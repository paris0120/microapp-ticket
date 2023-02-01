import dayjs from 'dayjs';

export interface ITicketAssignment {
  id?: number;
  issueId?: number;
  issueUuid?: string;
  username?: string;
  roleKey?: string;
  roleWeight?: number;
  departmentKey?: string;
  departmentWeight?: number;
  created?: string;
  modified?: string;
  accepted?: string | null;
  left?: string | null;
  closed?: string | null;
  archived?: string | null;
}

export const defaultValue: Readonly<ITicketAssignment> = {};
