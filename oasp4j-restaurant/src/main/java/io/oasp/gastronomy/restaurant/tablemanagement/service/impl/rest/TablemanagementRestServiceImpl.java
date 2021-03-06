package io.oasp.gastronomy.restaurant.tablemanagement.service.impl.rest;

import io.oasp.gastronomy.restaurant.general.common.api.constants.PermissionConstants;
import io.oasp.gastronomy.restaurant.tablemanagement.common.api.Table;
import io.oasp.gastronomy.restaurant.tablemanagement.common.api.datatype.TableState;
import io.oasp.gastronomy.restaurant.tablemanagement.logic.api.Tablemanagement;
import io.oasp.gastronomy.restaurant.tablemanagement.logic.api.to.TableEto;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.mmm.util.exception.api.ObjectNotFoundUserException;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * The service class for REST calls in order to execute the methods in {@link Tablemanagement}.
 *
 * @author agreul
 */
@Path("/tablemanagement")
@Named("TablemanagementRestService")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
@Validated
public class TablemanagementRestServiceImpl {

  private Tablemanagement tableManagement;

  /**
   * This method sets the field <tt>tableManagement</tt>.
   *
   * @param tableManagement the new value of the field ${bare_field_name}
   */
  @Inject
  public void setTableManagement(Tablemanagement tableManagement) {

    this.tableManagement = tableManagement;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("javadoc")
  @GET
  @Path("/table/{id}/")
  @RolesAllowed(PermissionConstants.FIND_TABLE)
  public TableEto getTable(@PathParam("id") String id) {

    Long idAsLong;
    if (id == null) {
      throw new BadRequestException("missing id");
    }
    try {
      idAsLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
      throw new BadRequestException("id is not a number");
    } catch (NotFoundException e) {
      throw new BadRequestException("table not found");
    }
    return this.tableManagement.findTable(idAsLong);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("javadoc")
  @GET
  @Path("/table/")
  @RolesAllowed(PermissionConstants.FIND_TABLE)
  public List<TableEto> getAllTables() {

    List<TableEto> allTables = this.tableManagement.findAllTables();
    return allTables;
  }

  /**
   * Creates a new restaurant table and store it in the database.
   *
   * @param table the table to be created
   * @return the recently created table
   */
  @POST
  @Path("/table/")
  @RolesAllowed(PermissionConstants.CREATE_TABLE)
  public TableEto createTable(@Valid TableEto table) {

    return this.tableManagement.createTable(table);
  }

  /**
   * Deletes the table with specified id.
   *
   * @param id the table to be deleted
   */
  @DELETE
  @Path("/table/{id}/")
  @RolesAllowed(PermissionConstants.DELETE_TABLE)
  public void deleteTable(@PathParam("id") Long id) {

    this.tableManagement.deleteTable(id);
  }

  /**
   * Returns a list of all existing free tables.
   *
   * @return List of all existing free tables
   */
  @GET
  @Path("/freetables/")
  @RolesAllowed(PermissionConstants.FIND_TABLE)
  public List<TableEto> getFreeTables() {

    return this.tableManagement.findFreeTables();
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("javadoc")
  @Path("/table/{id}/marktableas/{newState}")
  @POST
  @RolesAllowed(PermissionConstants.UPDATE_TABLE)
  public void markTableAs(@PathParam("id") Long id, @PathParam("newState") TableState newState) {

    TableEto table = this.tableManagement.findTable(id);
    if (table == null) {
      throw new ObjectNotFoundUserException(Table.class, id);
    } else {
      this.tableManagement.markTableAs(table, newState);
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("javadoc")
  @GET
  @Path("/table/{id}/istablereleasable/")
  @RolesAllowed(PermissionConstants.FIND_TABLE)
  public boolean isTableReleasable(@PathParam("id") Long id) {

    TableEto table = this.tableManagement.findTable(id);
    if (table == null) {
      throw new ObjectNotFoundUserException(Table.class, id);
    } else {
      return this.tableManagement.isTableReleasable(table);
    }
  }
}
