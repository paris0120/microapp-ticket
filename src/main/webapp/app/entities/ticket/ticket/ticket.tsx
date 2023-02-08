import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
// import { Button, Table } from 'reactstrap';
import { byteSize, Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITicket } from 'app/shared/model/ticket/ticket.model';
import { getEntities } from './ticket.reducer';
import { Avatar, Button, Col, Dropdown, List, MenuProps, Pagination, Radio, Row, Space, Table, TablePaginationConfig, Tag } from 'antd';
import { DownOutlined } from '@ant-design/icons';
import { ColumnsType } from 'antd/es/table';
import { data } from 'autoprefixer';
import TimeAgo from 'javascript-time-ago';
import en from 'javascript-time-ago/locale/en';
import { convertDateTimeToServer } from 'app/shared/util/date-utils';

const items: MenuProps['items'] = [
  {
    label: 'Newest',
    key: 'newest',
  },
  {
    label: 'Oldest',
    key: 'oldest',
  },
];
export const Ticket = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const onSortBy = e => {
    switch (e.key) {
      case 'newest':
        setPaginationState({
          ...paginationState,
          order: DESC,
          sort: 'updated',
        });
        break;
      case 'oldest':
        setPaginationState({
          ...paginationState,
          order: ASC,
          sort: 'updated',
        });
        break;
    }
  };
  const [isClosed, setIsClosed] = useState(0);
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'updated', 'desc'), location.search)
  );

  const ticketList = useAppSelector(state => state.ticket.ticket.entities);
  const loading = useAppSelector(state => state.ticket.ticket.loading);
  const totalItems = useAppSelector(state => state.ticket.ticket.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}&closed=${isClosed}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };
  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort, isClosed]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const timeAgo = new TimeAgo('en-US');
  return (
    <div>
      <h2 id="ticket-heading" data-cy="TicketHeading">
        <Translate contentKey="ticketApp.ticketTicket.home.title">Tickets</Translate>
      </h2>
      <div className="table-responsive">
        <Row>
          <Col span={24} {...{ align: 'right' }}>
            <span style={{ float: 'left' }}>
              <Radio.Group value={isClosed} onChange={e => setIsClosed(e.target.value)}>
                <Radio.Button value="0">Open</Radio.Button>
                <Radio.Button value="1">Closed</Radio.Button>
              </Radio.Group>
            </span>
            {ticketList && ticketList.length > 0 ? (
              <Dropdown menu={{ items, onClick: onSortBy }}>
                <Space>
                  Sort By
                  <DownOutlined />
                </Space>
              </Dropdown>
            ) : (
              ''
            )}

            <Button type="link" id="create" data-cy="entityCreateButton" href="/ticket/ticket/new" color="info">
              <FontAwesomeIcon icon="plus" />
              &nbsp;
              <Translate contentKey="ticketApp.ticketTicket.home.createLabel">Create new Ticket</Translate>
            </Button>
          </Col>
        </Row>

        {ticketList && ticketList.length > 0 ? (
          <>
            <Row>
              <Col span={24}>
                <List
                  itemLayout="horizontal"
                  dataSource={ticketList}
                  renderItem={(item: ITicket) => (
                    <List.Item>
                      <List.Item.Meta
                        avatar={<div>{item.typeKey}</div>}
                        title={
                          <div>
                            <a href={'/ticket/ticket/' + item.id}>{item.title}</a> {item.workflowStatusKey} {item.tags} {item.priorityLevel}
                          </div>
                        }
                        description={
                          <div>
                            <span style={{ float: 'left' }}>
                              {'#' + item.id + ' opened ' + timeAgo.format(convertDateTimeToServer(item.created)) + ' by ' + item.username}
                            </span>{' '}
                            <span {...{ align: 'right' }} style={{ display: 'block' }}>
                              <FontAwesomeIcon icon="comment" /> {item.totalComments}{' '}
                            </span>
                          </div>
                        }
                      />
                    </List.Item>
                  )}
                />
              </Col>
            </Row>
          </>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="ticketApp.ticketTicket.home.notFound">No Tickets found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={ticketList && ticketList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <Pagination
              defaultCurrent={paginationState.itemsPerPage}
              total={totalItems}
              pageSize={paginationState.itemsPerPage}
              onChange={handlePagination}
              showTotal={total => `Total ${total} open tickets`}
            />
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Ticket;
