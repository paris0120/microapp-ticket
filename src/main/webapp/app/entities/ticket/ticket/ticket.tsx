import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITicket } from 'app/shared/model/ticket/ticket.model';
import { getEntities } from './ticket.reducer';

export const Ticket = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const ticketList = useAppSelector(state => state.ticket.ticket.entities);
  const loading = useAppSelector(state => state.ticket.ticket.loading);
  const totalItems = useAppSelector(state => state.ticket.ticket.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="ticket-heading" data-cy="TicketHeading">
        <Translate contentKey="ticketApp.ticketTicket.home.title">Tickets</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="ticketApp.ticketTicket.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/ticket/ticket/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="ticketApp.ticketTicket.home.createLabel">Create new Ticket</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ticketList && ticketList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="ticketApp.ticketTicket.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('username')}>
                  <Translate contentKey="ticketApp.ticketTicket.username">Username</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('userFirstName')}>
                  <Translate contentKey="ticketApp.ticketTicket.userFirstName">User First Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('userLastName')}>
                  <Translate contentKey="ticketApp.ticketTicket.userLastName">User Last Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('userDisplayName')}>
                  <Translate contentKey="ticketApp.ticketTicket.userDisplayName">User Display Name</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('title')}>
                  <Translate contentKey="ticketApp.ticketTicket.title">Title</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('content')}>
                  <Translate contentKey="ticketApp.ticketTicket.content">Content</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('typeKey')}>
                  <Translate contentKey="ticketApp.ticketTicket.typeKey">Type Key</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('workflowStatusKey')}>
                  <Translate contentKey="ticketApp.ticketTicket.workflowStatusKey">Workflow Status Key</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('priorityLevel')}>
                  <Translate contentKey="ticketApp.ticketTicket.priorityLevel">Priority Level</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('tags')}>
                  <Translate contentKey="ticketApp.ticketTicket.tags">Tags</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('totalComments')}>
                  <Translate contentKey="ticketApp.ticketTicket.totalComments">Total Comments</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('uuid')}>
                  <Translate contentKey="ticketApp.ticketTicket.uuid">Uuid</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('created')}>
                  <Translate contentKey="ticketApp.ticketTicket.created">Created</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('modified')}>
                  <Translate contentKey="ticketApp.ticketTicket.modified">Modified</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updated')}>
                  <Translate contentKey="ticketApp.ticketTicket.updated">Updated</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('closed')}>
                  <Translate contentKey="ticketApp.ticketTicket.closed">Closed</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('archived')}>
                  <Translate contentKey="ticketApp.ticketTicket.archived">Archived</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ticketList.map((ticket, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/ticket/ticket/${ticket.id}`} color="link" size="sm">
                      {ticket.id}
                    </Button>
                  </td>
                  <td>{ticket.username}</td>
                  <td>{ticket.userFirstName}</td>
                  <td>{ticket.userLastName}</td>
                  <td>{ticket.userDisplayName}</td>
                  <td>{ticket.title}</td>
                  <td>{ticket.content}</td>
                  <td>{ticket.typeKey}</td>
                  <td>{ticket.workflowStatusKey}</td>
                  <td>{ticket.priorityLevel}</td>
                  <td>{ticket.tags}</td>
                  <td>{ticket.totalComments}</td>
                  <td>{ticket.uuid}</td>
                  <td>{ticket.created ? <TextFormat type="date" value={ticket.created} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{ticket.modified ? <TextFormat type="date" value={ticket.modified} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{ticket.updated ? <TextFormat type="date" value={ticket.updated} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{ticket.closed ? <TextFormat type="date" value={ticket.closed} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{ticket.archived ? <TextFormat type="date" value={ticket.archived} format={APP_DATE_FORMAT} /> : null}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/ticket/ticket/${ticket.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/ticket/ticket/${ticket.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        to={`/ticket/ticket/${ticket.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="ticketApp.ticketTicket.home.notFound">No Tickets found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={ticketList && ticketList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Ticket;
