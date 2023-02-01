import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ticket-type.reducer';

export const TicketTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ticketTypeEntity = useAppSelector(state => state.ticket.ticketType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ticketTypeDetailsHeading">
          <Translate contentKey="ticketApp.ticketTicketType.detail.title">TicketType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.id}</dd>
          <dt>
            <span id="key">
              <Translate contentKey="ticketApp.ticketTicketType.key">Key</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.key}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="ticketApp.ticketTicketType.type">Type</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.type}</dd>
          <dt>
            <span id="weight">
              <Translate contentKey="ticketApp.ticketTicketType.weight">Weight</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.weight}</dd>
          <dt>
            <span id="color">
              <Translate contentKey="ticketApp.ticketTicketType.color">Color</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.color}</dd>
          <dt>
            <span id="icon">
              <Translate contentKey="ticketApp.ticketTicketType.icon">Icon</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.icon}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="ticketApp.ticketTicketType.description">Description</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.description}</dd>
          <dt>
            <span id="parentType">
              <Translate contentKey="ticketApp.ticketTicketType.parentType">Parent Type</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.parentType}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="ticketApp.ticketTicketType.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="created">
              <Translate contentKey="ticketApp.ticketTicketType.created">Created</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.created ? <TextFormat value={ticketTypeEntity.created} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="modified">
              <Translate contentKey="ticketApp.ticketTicketType.modified">Modified</Translate>
            </span>
          </dt>
          <dd>
            {ticketTypeEntity.modified ? <TextFormat value={ticketTypeEntity.modified} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="archived">
              <Translate contentKey="ticketApp.ticketTicketType.archived">Archived</Translate>
            </span>
          </dt>
          <dd>
            {ticketTypeEntity.archived ? <TextFormat value={ticketTypeEntity.archived} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/ticket/ticket-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ticket/ticket-type/${ticketTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TicketTypeDetail;
