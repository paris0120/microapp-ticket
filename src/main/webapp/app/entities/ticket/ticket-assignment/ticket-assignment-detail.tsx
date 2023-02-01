import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ticket-assignment.reducer';

export const TicketAssignmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ticketAssignmentEntity = useAppSelector(state => state.ticket.ticketAssignment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ticketAssignmentDetailsHeading">
          <Translate contentKey="ticketApp.ticketTicketAssignment.detail.title">TicketAssignment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ticketAssignmentEntity.id}</dd>
          <dt>
            <span id="issueId">
              <Translate contentKey="ticketApp.ticketTicketAssignment.issueId">Issue Id</Translate>
            </span>
          </dt>
          <dd>{ticketAssignmentEntity.issueId}</dd>
          <dt>
            <span id="issueUuid">
              <Translate contentKey="ticketApp.ticketTicketAssignment.issueUuid">Issue Uuid</Translate>
            </span>
          </dt>
          <dd>{ticketAssignmentEntity.issueUuid}</dd>
          <dt>
            <span id="username">
              <Translate contentKey="ticketApp.ticketTicketAssignment.username">Username</Translate>
            </span>
          </dt>
          <dd>{ticketAssignmentEntity.username}</dd>
          <dt>
            <span id="roleKey">
              <Translate contentKey="ticketApp.ticketTicketAssignment.roleKey">Role Key</Translate>
            </span>
          </dt>
          <dd>{ticketAssignmentEntity.roleKey}</dd>
          <dt>
            <span id="roleWeight">
              <Translate contentKey="ticketApp.ticketTicketAssignment.roleWeight">Role Weight</Translate>
            </span>
          </dt>
          <dd>{ticketAssignmentEntity.roleWeight}</dd>
          <dt>
            <span id="departmentKey">
              <Translate contentKey="ticketApp.ticketTicketAssignment.departmentKey">Department Key</Translate>
            </span>
          </dt>
          <dd>{ticketAssignmentEntity.departmentKey}</dd>
          <dt>
            <span id="departmentWeight">
              <Translate contentKey="ticketApp.ticketTicketAssignment.departmentWeight">Department Weight</Translate>
            </span>
          </dt>
          <dd>{ticketAssignmentEntity.departmentWeight}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="ticketApp.ticketTicketAssignment.created">Created</Translate>
            </span>
          </dt>
          <dd>
            {ticketAssignmentEntity.created ? (
              <TextFormat value={ticketAssignmentEntity.created} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="modified">
              <Translate contentKey="ticketApp.ticketTicketAssignment.modified">Modified</Translate>
            </span>
          </dt>
          <dd>
            {ticketAssignmentEntity.modified ? (
              <TextFormat value={ticketAssignmentEntity.modified} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="accepted">
              <Translate contentKey="ticketApp.ticketTicketAssignment.accepted">Accepted</Translate>
            </span>
          </dt>
          <dd>
            {ticketAssignmentEntity.accepted ? (
              <TextFormat value={ticketAssignmentEntity.accepted} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="left">
              <Translate contentKey="ticketApp.ticketTicketAssignment.left">Left</Translate>
            </span>
          </dt>
          <dd>
            {ticketAssignmentEntity.left ? <TextFormat value={ticketAssignmentEntity.left} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="closed">
              <Translate contentKey="ticketApp.ticketTicketAssignment.closed">Closed</Translate>
            </span>
          </dt>
          <dd>
            {ticketAssignmentEntity.closed ? (
              <TextFormat value={ticketAssignmentEntity.closed} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="archived">
              <Translate contentKey="ticketApp.ticketTicketAssignment.archived">Archived</Translate>
            </span>
          </dt>
          <dd>
            {ticketAssignmentEntity.archived ? (
              <TextFormat value={ticketAssignmentEntity.archived} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/ticket/ticket-assignment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ticket/ticket-assignment/${ticketAssignmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TicketAssignmentDetail;
