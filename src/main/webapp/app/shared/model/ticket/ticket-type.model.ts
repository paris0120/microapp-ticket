import dayjs from 'dayjs';

export interface ITicketType {
  id?: number;
  key?: string;
  type?: string;
  weight?: number;
  color?: string | null;
  icon?: string | null;
  description?: string;
  parentType?: string;
  isActive?: boolean;
  created?: string;
  modified?: string;
  archived?: string | null;
}

export const defaultValue: Readonly<ITicketType> = {
  isActive: false,
};
