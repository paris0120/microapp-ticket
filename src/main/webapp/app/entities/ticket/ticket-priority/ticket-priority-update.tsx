import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITicketPriority } from 'app/shared/model/ticket/ticket-priority.model';
import { getEntity, updateEntity, createEntity, reset } from './ticket-priority.reducer';

export const TicketPriorityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ticketPriorityEntity = useAppSelector(state => state.ticket.ticketPriority.entity);
  const loading = useAppSelector(state => state.ticket.ticketPriority.loading);
  const updating = useAppSelector(state => state.ticket.ticketPriority.updating);
  const updateSuccess = useAppSelector(state => state.ticket.ticketPriority.updateSuccess);

  const handleClose = () => {
    navigate('/ticket/ticket-priority');
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
    const entity = {
      ...ticketPriorityEntity,
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
      ? {}
      : {
          ...ticketPriorityEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ticketApp.ticketTicketPriority.home.createOrEditLabel" data-cy="TicketPriorityCreateUpdateHeading">
            <Translate contentKey="ticketApp.ticketTicketPriority.home.createOrEditLabel">Create or edit a TicketPriority</Translate>
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
                  id="ticket-priority-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('ticketApp.ticketTicketPriority.priorityLevel')}
                id="ticket-priority-priorityLevel"
                name="priorityLevel"
                data-cy="priorityLevel"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketPriority.priority')}
                id="ticket-priority-priority"
                name="priority"
                data-cy="priority"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketPriority.color')}
                id="ticket-priority-color"
                name="color"
                data-cy="color"
                type="text"
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketPriority.icon')}
                id="ticket-priority-icon"
                name="icon"
                data-cy="icon"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ticket/ticket-priority" replace color="info">
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

export default TicketPriorityUpdate;
