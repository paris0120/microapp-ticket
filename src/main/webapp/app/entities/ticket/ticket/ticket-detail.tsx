import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Translate, byteSize, TextFormat, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, updateEntity } from './ticket.reducer';
import { Button, Card, Col, Divider, Form, Input, Row, Space, Typography } from 'antd';
import Paragraph from 'antd/es/typography/Paragraph';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import TimeAgo from 'javascript-time-ago';
import { IconName } from '@fortawesome/fontawesome-common-types';

const CommentEntitiesMenuItems = React.lazy(() => import('@comment/entities-menu').catch(() => import('app/shared/error/error-loading')));

export const TicketDetail = () => {
  const dispatch = useAppDispatch();

  const { Text, Link } = Typography;
  const { id } = useParams<'id'>();
  const [status, setStatus] = useState('display');
  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const saveEntity = values => {
    values.created = 0;
    values.modified = convertDateTimeToServer(values.modified);
    values.updated = convertDateTimeToServer(values.updated);
    values.closed = convertDateTimeToServer(values.closed);
    values.archived = convertDateTimeToServer(values.archived);
    values.typeKey = ticketEntity.typeKey;
    const entity = {
      ...ticketEntity,
      ...values,
    };

    dispatch(updateEntity(entity));
    setStatus('display');
  };

  const ticketEntity = useAppSelector(state => state.ticket.ticket.entity);

  const timeAgo = new TimeAgo('en-US');
  return (
    <Form initialValues={ticketEntity} onFinish={saveEntity}>
      <Row>
        <Col md="8" offset={1} span={22}>
          {status == 'title' ? (
            <Form.Item
              id="ticket-title"
              name="title"
              data-cy="title"
              rules={[
                { required: true, message: translate('entity.validation.required') },
                { min: 2, message: translate('entity.validation.minlength', { min: 2 }) },
              ]}
            >
              <Input defaultValue={ticketEntity.title} placeholder={translate('ticketApp.ticketTicket.title')} />
            </Form.Item>
          ) : (
            <div>
              <span>
                <h2 style={{ display: 'inline' }}>{ticketEntity.title}</h2>{' '}
                <a style={{ display: 'inline', float: 'right' }} onClick={event => setStatus('title')}>
                  Edit Title
                </a>
              </span>
            </div>
          )}
          <div>
            <div>
              {ticketEntity.ticketType ? (
                <Space>
                  <FontAwesomeIcon
                    title={ticketEntity.ticketType.type + ': ' + ticketEntity.ticketPriority.priority}
                    icon={ticketEntity.ticketType.icon as IconName}
                  />
                  <Text>{ticketEntity.ticketType.type}</Text>
                  <Text style={{ color: ticketEntity.ticketPriority.color }}>{ticketEntity.ticketPriority.priority}</Text>
                  <Text> Status: {ticketEntity.workflowStatusKey}</Text>
                </Space>
              ) : (
                ''
              )}{' '}
              {ticketEntity.tags}{' '}
            </div>
          </div>
        </Col>
      </Row>
      <Row>
        <Col md="8" offset={1} span={22}>
          <div>
            <span style={{ float: 'left' }}>
              {'#' + ticketEntity.id + ' opened by ' + ticketEntity.username}{' '}
              {ticketEntity.created ? <TextFormat value={ticketEntity.created} type="date" format={'on ' + APP_DATE_FORMAT} /> : null}
            </span>
            <span {...{ align: 'right' }} style={{ display: 'block' }}>
              <a onClick={event => setStatus('editContent')}>Edit Content</a>
            </span>
          </div>
        </Col>
      </Row>
      <Row>
        <Col md="8" offset={1} span={22}>
          <Divider></Divider>

          {status == 'editContent' ? (
            <>
              <Form.Item
                id="ticket-content"
                name="content"
                data-cy="content"
                rules={[{ required: true, message: translate('entity.validation.required') }]}
              >
                <Input.TextArea style={{ height: 400 }} placeholder={translate('ticketApp.ticketTicket.content')} />
              </Form.Item>

              <Form.Item>
                <Button type="primary" htmlType="submit">
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </Form.Item>
            </>
          ) : (
            <div>
              <Paragraph>{ticketEntity.content}</Paragraph>
            </div>
          )}

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
        </Col>
      </Row>
    </Form>
  );
};

export default TicketDetail;
