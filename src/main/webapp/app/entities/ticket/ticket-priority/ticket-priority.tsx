import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITicketPriority } from 'app/shared/model/ticket/ticket-priority.model';
import { getEntities } from './ticket-priority.reducer';

export const TicketPriority = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const ticketPriorityList = useAppSelector(state => state.ticket.ticketPriority.entities);
  const loading = useAppSelector(state => state.ticket.ticketPriority.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="ticket-priority-heading" data-cy="TicketPriorityHeading">
        <Translate contentKey="ticketApp.ticketTicketPriority.home.title">Ticket Priorities</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="ticketApp.ticketTicketPriority.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/ticket/ticket-priority/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="ticketApp.ticketTicketPriority.home.createLabel">Create new Ticket Priority</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ticketPriorityList && ticketPriorityList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketPriority.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketPriority.priorityLevel">Priority Level</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketPriority.priority">Priority</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketPriority.color">Color</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketPriority.icon">Icon</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ticketPriorityList.map((ticketPriority, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/ticket/ticket-priority/${ticketPriority.id}`} color="link" size="sm">
                      {ticketPriority.id}
                    </Button>
                  </td>
                  <td>{ticketPriority.priorityLevel}</td>
                  <td>{ticketPriority.priority}</td>
                  <td>{ticketPriority.color}</td>
                  <td>{ticketPriority.icon}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/ticket/ticket-priority/${ticketPriority.id}`}
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
                        to={`/ticket/ticket-priority/${ticketPriority.id}/edit`}
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
                        to={`/ticket/ticket-priority/${ticketPriority.id}/delete`}
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
              <Translate contentKey="ticketApp.ticketTicketPriority.home.notFound">No Ticket Priorities found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default TicketPriority;
