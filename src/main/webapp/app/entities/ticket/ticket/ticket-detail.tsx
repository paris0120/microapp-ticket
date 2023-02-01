import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ticket.reducer';

export const TicketDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ticketEntity = useAppSelector(state => state.ticket.ticket.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ticketDetailsHeading">
          <Translate contentKey="ticketApp.ticketTicket.detail.title">Ticket</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.id}</dd>
          <dt>
            <span id="username">
              <Translate contentKey="ticketApp.ticketTicket.username">Username</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.username}</dd>
          <dt>
            <span id="userFirstName">
              <Translate contentKey="ticketApp.ticketTicket.userFirstName">User First Name</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.userFirstName}</dd>
          <dt>
            <span id="userLastName">
              <Translate contentKey="ticketApp.ticketTicket.userLastName">User Last Name</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.userLastName}</dd>
          <dt>
            <span id="userDisplayName">
              <Translate contentKey="ticketApp.ticketTicket.userDisplayName">User Display Name</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.userDisplayName}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="ticketApp.ticketTicket.title">Title</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="ticketApp.ticketTicket.content">Content</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.content}</dd>
          <dt>
            <span id="typeKey">
              <Translate contentKey="ticketApp.ticketTicket.typeKey">Type Key</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.typeKey}</dd>
          <dt>
            <span id="workflowStatusKey">
              <Translate contentKey="ticketApp.ticketTicket.workflowStatusKey">Workflow Status Key</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.workflowStatusKey}</dd>
          <dt>
            <span id="priorityLevel">
              <Translate contentKey="ticketApp.ticketTicket.priorityLevel">Priority Level</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.priorityLevel}</dd>
          <dt>
            <span id="tags">
              <Translate contentKey="ticketApp.ticketTicket.tags">Tags</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.tags}</dd>
          <dt>
            <span id="totalComments">
              <Translate contentKey="ticketApp.ticketTicket.totalComments">Total Comments</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.totalComments}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="ticketApp.ticketTicket.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.uuid}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="ticketApp.ticketTicket.created">Created</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.created ? <TextFormat value={ticketEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="modified">
              <Translate contentKey="ticketApp.ticketTicket.modified">Modified</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.modified ? <TextFormat value={ticketEntity.modified} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updated">
              <Translate contentKey="ticketApp.ticketTicket.updated">Updated</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.updated ? <TextFormat value={ticketEntity.updated} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="closed">
              <Translate contentKey="ticketApp.ticketTicket.closed">Closed</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.closed ? <TextFormat value={ticketEntity.closed} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="archived">
              <Translate contentKey="ticketApp.ticketTicket.archived">Archived</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.archived ? <TextFormat value={ticketEntity.archived} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/ticket/ticket" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ticket/ticket/${ticketEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TicketDetail;
