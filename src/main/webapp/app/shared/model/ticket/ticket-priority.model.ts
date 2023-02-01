export interface ITicketPriority {
  id?: number;
  priorityLevel?: number;
  priority?: string;
  color?: string | null;
  icon?: string | null;
}

export const defaultValue: Readonly<ITicketPriority> = {};
