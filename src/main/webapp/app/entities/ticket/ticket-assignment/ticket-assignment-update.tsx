import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITicketAssignment } from 'app/shared/model/ticket/ticket-assignment.model';
import { getEntity, updateEntity, createEntity, reset } from './ticket-assignment.reducer';

export const TicketAssignmentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ticketAssignmentEntity = useAppSelector(state => state.ticket.ticketAssignment.entity);
  const loading = useAppSelector(state => state.ticket.ticketAssignment.loading);
  const updating = useAppSelector(state => state.ticket.ticketAssignment.updating);
  const updateSuccess = useAppSelector(state => state.ticket.ticketAssignment.updateSuccess);

  const handleClose = () => {
    navigate('/ticket/ticket-assignment');
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
    values.accepted = convertDateTimeToServer(values.accepted);
    values.left = convertDateTimeToServer(values.left);
    values.closed = convertDateTimeToServer(values.closed);
    values.archived = convertDateTimeToServer(values.archived);

    const entity = {
      ...ticketAssignmentEntity,
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
          accepted: displayDefaultDateTime(),
          left: displayDefaultDateTime(),
          closed: displayDefaultDateTime(),
          archived: displayDefaultDateTime(),
        }
      : {
          ...ticketAssignmentEntity,
          created: convertDateTimeFromServer(ticketAssignmentEntity.created),
          modified: convertDateTimeFromServer(ticketAssignmentEntity.modified),
          accepted: convertDateTimeFromServer(ticketAssignmentEntity.accepted),
          left: convertDateTimeFromServer(ticketAssignmentEntity.left),
          closed: convertDateTimeFromServer(ticketAssignmentEntity.closed),
          archived: convertDateTimeFromServer(ticketAssignmentEntity.archived),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ticketApp.ticketTicketAssignment.home.createOrEditLabel" data-cy="TicketAssignmentCreateUpdateHeading">
            <Translate contentKey="ticketApp.ticketTicketAssignment.home.createOrEditLabel">Create or edit a TicketAssignment</Translate>
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
                  id="ticket-assignment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('ticketApp.ticketTicketAssignment.issueId')}
                id="ticket-assignment-issueId"
                name="issueId"
                data-cy="issueId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketAssignment.issueUuid')}
                id="ticket-assignment-issueUuid"
                name="issueUuid"
                data-cy="issueUuid"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketAssignment.username')}
                id="ticket-assignment-username"
                name="username"
                data-cy="username"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketAssignment.roleKey')}
                id="ticket-assignment-roleKey"
                name="roleKey"
                data-cy="roleKey"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketAssignment.roleWeight')}
                id="ticket-assignment-roleWeight"
                name="roleWeight"
                data-cy="roleWeight"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketAssignment.isManager')}
                id="ticket-assignment-isManager"
                name="isManager"
                data-cy="isManager"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketAssignment.departmentKey')}
                id="ticket-assignment-departmentKey"
                name="departmentKey"
                data-cy="departmentKey"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketAssignment.departmentWeight')}
                id="ticket-assignment-departmentWeight"
                name="departmentWeight"
                data-cy="departmentWeight"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketAssignment.assignedByUsername')}
                id="ticket-assignment-assignedByUsername"
                name="assignedByUsername"
                data-cy="assignedByUsername"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketAssignment.created')}
                id="ticket-assignment-created"
                name="created"
                data-cy="created"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketAssignment.modified')}
                id="ticket-assignment-modified"
                name="modified"
                data-cy="modified"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketAssignment.accepted')}
                id="ticket-assignment-accepted"
                name="accepted"
                data-cy="accepted"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketAssignment.left')}
                id="ticket-assignment-left"
                name="left"
                data-cy="left"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketAssignment.closed')}
                id="ticket-assignment-closed"
                name="closed"
                data-cy="closed"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('ticketApp.ticketTicketAssignment.archived')}
                id="ticket-assignment-archived"
                name="archived"
                data-cy="archived"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ticket/ticket-assignment" replace color="info">
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

export default TicketAssignmentUpdate;
