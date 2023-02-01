import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITicketAssignment } from 'app/shared/model/ticket/ticket-assignment.model';
import { getEntities } from './ticket-assignment.reducer';

export const TicketAssignment = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const ticketAssignmentList = useAppSelector(state => state.ticket.ticketAssignment.entities);
  const loading = useAppSelector(state => state.ticket.ticketAssignment.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="ticket-assignment-heading" data-cy="TicketAssignmentHeading">
        <Translate contentKey="ticketApp.ticketTicketAssignment.home.title">Ticket Assignments</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="ticketApp.ticketTicketAssignment.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/ticket/ticket-assignment/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="ticketApp.ticketTicketAssignment.home.createLabel">Create new Ticket Assignment</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ticketAssignmentList && ticketAssignmentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketAssignment.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketAssignment.issueId">Issue Id</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketAssignment.issueUuid">Issue Uuid</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketAssignment.username">Username</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketAssignment.roleKey">Role Key</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketAssignment.roleWeight">Role Weight</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketAssignment.departmentKey">Department Key</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketAssignment.departmentWeight">Department Weight</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketAssignment.created">Created</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketAssignment.modified">Modified</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketAssignment.accepted">Accepted</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketAssignment.left">Left</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketAssignment.closed">Closed</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketAssignment.archived">Archived</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ticketAssignmentList.map((ticketAssignment, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/ticket/ticket-assignment/${ticketAssignment.id}`} color="link" size="sm">
                      {ticketAssignment.id}
                    </Button>
                  </td>
                  <td>{ticketAssignment.issueId}</td>
                  <td>{ticketAssignment.issueUuid}</td>
                  <td>{ticketAssignment.username}</td>
                  <td>{ticketAssignment.roleKey}</td>
                  <td>{ticketAssignment.roleWeight}</td>
                  <td>{ticketAssignment.departmentKey}</td>
                  <td>{ticketAssignment.departmentWeight}</td>
                  <td>
                    {ticketAssignment.created ? <TextFormat type="date" value={ticketAssignment.created} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {ticketAssignment.modified ? (
                      <TextFormat type="date" value={ticketAssignment.modified} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {ticketAssignment.accepted ? (
                      <TextFormat type="date" value={ticketAssignment.accepted} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {ticketAssignment.left ? <TextFormat type="date" value={ticketAssignment.left} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {ticketAssignment.closed ? <TextFormat type="date" value={ticketAssignment.closed} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {ticketAssignment.archived ? (
                      <TextFormat type="date" value={ticketAssignment.archived} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/ticket/ticket-assignment/${ticketAssignment.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/ticket/ticket-assignment/${ticketAssignment.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/ticket/ticket-assignment/${ticketAssignment.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="ticketApp.ticketTicketAssignment.home.notFound">No Ticket Assignments found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default TicketAssignment;
