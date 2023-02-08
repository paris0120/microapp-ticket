import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITicketType } from 'app/shared/model/ticket/ticket-type.model';
import { getEntity, updateEntity, createEntity, reset } from './ticket-type.reducer';

export const TicketTypeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ticketTypeEntity = useAppSelector(state => state.ticket.ticketType.entity);
  const loading = useAppSelector(state => state.ticket.ticketType.loading);
  const updating = useAppSelector(state => state.ticket.ticketType.updating);
  const updateSuccess = useAppSelector(state => state.ticket.ticketType.updateSuccess);

  const handleClose = () => {
    navigate('/ticket/ticket-type');
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
    values.archived = convertDateTimeToServer(values.archived);

    const entity = {
      ...ticketTypeEntity,
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
          archived: displayDefaultDateTime(),
        }
      : {
          ...ticketTypeEntity,
          created: convertDateTimeFromServer(ticketTypeEntity.created),
          modified: convertDateTimeFromServer(ticketTypeEntity.modified),
          archived: convertDateTimeFromServer(ticketTypeEntity.archived),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ticketApp.ticketTicketType.home.createOrEditLabel" data-cy="TicketTypeCreateUpdateHeading">
            <Translate contentKey="ticketApp.ticketTicketType.home.createOrEditLabel">Create or edit a TicketType</Translate>
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
                  id="ticket-type-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('ticketApp.ticketTicketType.key')}
                id="ticket-type-key"
                name="key"
                data-cy="key"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketType.type')}
                id="ticket-type-type"
                name="type"
                data-cy="type"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketType.weight')}
                id="ticket-type-weight"
                name="weight"
                data-cy="weight"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketType.color')}
                id="ticket-type-color"
                name="color"
                data-cy="color"
                type="text"
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketType.icon')}
                id="ticket-type-icon"
                name="icon"
                data-cy="icon"
                type="text"
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketType.description')}
                id="ticket-type-description"
                name="description"
                data-cy="description"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketType.isActive')}
                id="ticket-type-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketType.created')}
                id="ticket-type-created"
                name="created"
                data-cy="created"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketType.modified')}
                id="ticket-type-modified"
                name="modified"
                data-cy="modified"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketType.archived')}
                id="ticket-type-archived"
                name="archived"
                data-cy="archived"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ticket/ticket-type" replace color="info">
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

export default TicketTypeUpdate;
