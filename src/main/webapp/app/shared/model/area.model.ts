import dayjs from 'dayjs';

export interface IArea {
  id?: number;
  name?: string;
  createOn?: string | null;
  updatedOn?: string | null;
}

export const defaultValue: Readonly<IArea> = {};
