import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITicket } from 'app/shared/model/ticket/ticket.model';
import { getEntity, updateEntity, createEntity, reset } from './ticket.reducer';

export const TicketUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ticketEntity = useAppSelector(state => state.ticket.ticket.entity);
  const loading = useAppSelector(state => state.ticket.ticket.loading);
  const updating = useAppSelector(state => state.ticket.ticket.updating);
  const updateSuccess = useAppSelector(state => state.ticket.ticket.updateSuccess);

  const handleClose = () => {
    navigate('/ticket/ticket' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.created = convertDateTimeToServer(values.created);
    values.modified = convertDateTimeToServer(values.modified);
    values.updated = convertDateTimeToServer(values.updated);
    values.closed = convertDateTimeToServer(values.closed);
    values.archived = convertDateTimeToServer(values.archived);

    const entity = {
      ...ticketEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          created: displayDefaultDateTime(),
          modified: displayDefaultDateTime(),
          updated: displayDefaultDateTime(),
          closed: displayDefaultDateTime(),
          archived: displayDefaultDateTime(),
        }
      : {
          ...ticketEntity,
          created: convertDateTimeFromServer(ticketEntity.created),
          modified: convertDateTimeFromServer(ticketEntity.modified),
          updated: convertDateTimeFromServer(ticketEntity.updated),
          closed: convertDateTimeFromServer(ticketEntity.closed),
          archived: convertDateTimeFromServer(ticketEntity.archived),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ticketApp.ticketTicket.home.createOrEditLabel" data-cy="TicketCreateUpdateHeading">
            <Translate contentKey="ticketApp.ticketTicket.home.createOrEditLabel">Create or edit a Ticket</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="ticket-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('ticketApp.ticketTicket.username')}
                id="ticket-username"
                name="username"
                data-cy="username"
                type="text"
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicket.userFirstName')}
                id="ticket-userFirstName"
                name="userFirstName"
                data-cy="userFirstName"
                type="text"
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicket.userLastName')}
                id="ticket-userLastName"
                name="userLastName"
                data-cy="userLastName"
                type="text"
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicket.userDisplayName')}
                id="ticket-userDisplayName"
                name="userDisplayName"
                data-cy="userDisplayName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicket.title')}
                id="ticket-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicket.content')}
                id="ticket-content"
                name="content"
                data-cy="content"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicket.typeKey')}
                id="ticket-typeKey"
                name="typeKey"
                data-cy="typeKey"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicket.workflowStatusKey')}
                id="ticket-workflowStatusKey"
                name="workflowStatusKey"
                data-cy="workflowStatusKey"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicket.priorityLevel')}
                id="ticket-priorityLevel"
                name="priorityLevel"
                data-cy="priorityLevel"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicket.tags')}
                id="ticket-tags"
                name="tags"
                data-cy="tags"
                type="textarea"
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicket.totalComments')}
                id="ticket-totalComments"
                name="totalComments"
                data-cy="totalComments"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField label={translate('ticketApp.ticketTicket.uuid')} id="ticket-uuid" name="uuid" data-cy="uuid" type="text" />
              <ValidatedField
                label={translate('ticketApp.ticketTicket.created')}
                id="ticket-created"
                name="created"
                data-cy="created"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicket.modified')}
                id="ticket-modified"
                name="modified"
                data-cy="modified"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicket.updated')}
                id="ticket-updated"
                name="updated"
                data-cy="updated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicket.closed')}
                id="ticket-closed"
                name="closed"
                data-cy="closed"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicket.archived')}
                id="ticket-archived"
                name="archived"
                data-cy="archived"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ticket/ticket" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TicketUpdate;
