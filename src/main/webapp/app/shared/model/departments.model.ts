import dayjs from 'dayjs';

export interface IDepartments {
  id?: number;
  name?: string;
  createOn?: string | null;
  updatedOn?: string | null;
}

export const defaultValue: Readonly<IDepartments> = {};
