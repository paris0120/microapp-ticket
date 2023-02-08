import dayjs from 'dayjs';

export interface ITicket {
  id?: number;
  username?: string | null;
  userFirstName?: string | null;
  userLastName?: string | null;
  userDisplayName?: string | null;
  title?: string;
  content?: string;
  typeKey?: string;
  workflowStatusKey?: string;
  priorityLevel?: number;
  tags?: string | null;
  totalComments?: number;
  uuid?: string | null;
  created?: string;
  modified?: string;
  updated?: string;
  closed?: string | null;
  archived?: string | null;
}

export const defaultValue: Readonly<ITicket> = {};
