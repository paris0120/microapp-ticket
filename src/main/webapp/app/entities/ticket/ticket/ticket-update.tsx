import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
// import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITicket } from 'app/shared/model/ticket/ticket.model';
import { getEntity, updateEntity, createEntity, reset } from './ticket.reducer';

import { Button, Form, FormInstance, Input, List, Select, Space, Tooltip, Typography } from 'antd';
import { Col, Row } from 'antd';
import { getEntities } from 'app/entities/ticket/ticket-type/ticket-type.reducer';
import { ITicketType } from 'app/shared/model/ticket/ticket-type.model';
import { solid, regular, brands, icon } from '@fortawesome/fontawesome-svg-core/import.macro';
import { IconName } from '@fortawesome/fontawesome-common-types'; // <-- import styles to be used

export const TicketUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ticketEntity = useAppSelector(state => state.ticket.ticket.entity);
  const loading = useAppSelector(state => state.ticket.ticket.loading);
  const updating = useAppSelector(state => state.ticket.ticket.updating);
  const updateSuccess = useAppSelector(state => state.ticket.ticket.updateSuccess);
  const ticketTypeList = useAppSelector(state => state.ticket.ticketType.entities);
  const [ticketTypes, setTicketTypes] = useState(null);
  const handleClose = () => {
    ``;
    navigate('/ticket/ticket' + location.search);
  };
  useEffect(() => {
    dispatch(getEntities({}));
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

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [location.search]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    setTypeState(isNew ? params.get('type') : ticketEntity.typeKey);
  }, [location.search]);

  const [typeState, setTypeState] = useState(isNew ? new URLSearchParams(location.search).get('type') : ticketEntity.typeKey);
  const saveEntity = values => {
    console.log(values);
    values.created = 0;
    values.modified = convertDateTimeToServer(values.modified);
    values.updated = convertDateTimeToServer(values.updated);
    values.closed = convertDateTimeToServer(values.closed);
    values.archived = convertDateTimeToServer(values.archived);
    values.typeKey = isNew ? typeState : ticketEntity.typeKey;
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
      {typeState || isNew == false ? (
        <>
          <Row className="justify-content-center">
            <Col span={24}>
              <h2 id="ticketApp.ticketTicket.home.createOrEditLabel" data-cy="TicketCreateUpdateHeading">
                <Translate contentKey="ticketApp.ticketTicket.home.createOrEditLabel">Create or edit a Ticket</Translate>
              </h2>
            </Col>
          </Row>
          <Row className="justify-content-center">
            <Col span={24}>
              {loading ? (
                <p>Loading...</p>
              ) : (
                <Form initialValues={defaultValues()} onFinish={saveEntity}>
                  <Form.Item
                    id="ticket-title"
                    name="title"
                    data-cy="title"
                    rules={[
                      { required: true, message: translate('entity.validation.required') },
                      { min: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                    ]}
                  >
                    <Input placeholder={translate('ticketApp.ticketTicket.title')} />
                  </Form.Item>
                  <Form.Item
                    id="ticket-content"
                    name="content"
                    data-cy="content"
                    rules={[{ required: true, message: translate('entity.validation.required') }]}
                  >
                    <Input.TextArea style={{ height: 400 }} placeholder={translate('ticketApp.ticketTicket.content')} />
                  </Form.Item>

                  <Form.Item>
                    <Button type="link" id="cancel-save" data-cy="entityCreateCancelButton" href="/ticket/ticket" color="info">
                      <FontAwesomeIcon icon="arrow-left" />
                      &nbsp;
                      <Translate contentKey="entity.action.back">Back</Translate>
                    </Button>

                    <Button type="primary" htmlType="submit">
                      <FontAwesomeIcon icon="save" />
                      &nbsp;
                      <Translate contentKey="entity.action.save">Save</Translate>
                    </Button>
                  </Form.Item>
                </Form>
              )}
            </Col>
          </Row>
        </>
      ) : (
        <Row>
          <Col span={24}>
            <List
              itemLayout="horizontal"
              dataSource={ticketTypeList.filter(type => type.isActive == true)}
              renderItem={(item: ITicketType) => (
                <List.Item>
                  <List.Item.Meta
                    avatar={<FontAwesomeIcon icon={item.icon as IconName} width={30} />}
                    title={<a href={'/ticket/ticket/new?type=' + item.key}>{item.type}</a>}
                    description={item.description}
                  />
                </List.Item>
              )}
            />
          </Col>
        </Row>
      )}
    </div>
  );
};

export default TicketUpdate;
