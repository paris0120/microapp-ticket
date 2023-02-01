import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ticket-priority.reducer';

export const TicketPriorityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ticketPriorityEntity = useAppSelector(state => state.ticket.ticketPriority.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ticketPriorityDetailsHeading">
          <Translate contentKey="ticketApp.ticketTicketPriority.detail.title">TicketPriority</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ticketPriorityEntity.id}</dd>
          <dt>
            <span id="priorityLevel">
              <Translate contentKey="ticketApp.ticketTicketPriority.priorityLevel">Priority Level</Translate>
            </span>
          </dt>
          <dd>{ticketPriorityEntity.priorityLevel}</dd>
          <dt>
            <span id="priority">
              <Translate contentKey="ticketApp.ticketTicketPriority.priority">Priority</Translate>
            </span>
          </dt>
          <dd>{ticketPriorityEntity.priority}</dd>
          <dt>
            <span id="color">
              <Translate contentKey="ticketApp.ticketTicketPriority.color">Color</Translate>
            </span>
          </dt>
          <dd>{ticketPriorityEntity.color}</dd>
          <dt>
            <span id="icon">
              <Translate contentKey="ticketApp.ticketTicketPriority.icon">Icon</Translate>
            </span>
          </dt>
          <dd>{ticketPriorityEntity.icon}</dd>
        </dl>
        <Button tag={Link} to="/ticket/ticket-priority" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ticket/ticket-priority/${ticketPriorityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TicketPriorityDetail;
