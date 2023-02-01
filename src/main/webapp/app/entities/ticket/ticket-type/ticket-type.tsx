import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITicketType } from 'app/shared/model/ticket/ticket-type.model';
import { getEntities } from './ticket-type.reducer';

export const TicketType = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const ticketTypeList = useAppSelector(state => state.ticket.ticketType.entities);
  const loading = useAppSelector(state => state.ticket.ticketType.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="ticket-type-heading" data-cy="TicketTypeHeading">
        <Translate contentKey="ticketApp.ticketTicketType.home.title">Ticket Types</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="ticketApp.ticketTicketType.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/ticket/ticket-type/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="ticketApp.ticketTicketType.home.createLabel">Create new Ticket Type</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ticketTypeList && ticketTypeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketType.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketType.key">Key</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketType.type">Type</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketType.weight">Weight</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketType.color">Color</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketType.icon">Icon</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketType.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketType.parentType">Parent Type</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketType.isActive">Is Active</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketType.created">Created</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketType.modified">Modified</Translate>
                </th>
                <th>
                  <Translate contentKey="ticketApp.ticketTicketType.archived">Archived</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ticketTypeList.map((ticketType, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/ticket/ticket-type/${ticketType.id}`} color="link" size="sm">
                      {ticketType.id}
                    </Button>
                  </td>
                  <td>{ticketType.key}</td>
                  <td>{ticketType.type}</td>
                  <td>{ticketType.weight}</td>
                  <td>{ticketType.color}</td>
                  <td>{ticketType.icon}</td>
                  <td>{ticketType.description}</td>
                  <td>{ticketType.parentType}</td>
                  <td>{ticketType.isActive ? 'true' : 'false'}</td>
                  <td>{ticketType.created ? <TextFormat type="date" value={ticketType.created} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{ticketType.modified ? <TextFormat type="date" value={ticketType.modified} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{ticketType.archived ? <TextFormat type="date" value={ticketType.archived} format={APP_DATE_FORMAT} /> : null}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/ticket/ticket-type/${ticketType.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/ticket/ticket-type/${ticketType.id}/edit`}
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
                        to={`/ticket/ticket-type/${ticketType.id}/delete`}
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
              <Translate contentKey="ticketApp.ticketTicketType.home.notFound">No Ticket Types found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default TicketType;
