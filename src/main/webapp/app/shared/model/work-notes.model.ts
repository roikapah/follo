import dayjs from 'dayjs';
import { IPersons } from 'app/shared/model/persons.model';
import { ITask } from 'app/shared/model/task.model';

export interface IWorkNotes {
  id?: number;
  text?: string;
  createOn?: string;
  createBy?: IPersons | null;
  task?: ITask | null;
}

export const defaultValue: Readonly<IWorkNotes> = {};
