import dayjs from 'dayjs';

export interface ITaskType {
  id?: number;
  name?: string;
  createOn?: string | null;
  updatedOn?: string | null;
}

export const defaultValue: Readonly<ITaskType> = {};
