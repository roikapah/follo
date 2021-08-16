import dayjs from 'dayjs';
import { IPersons } from 'app/shared/model/persons.model';
import { IDepartments } from 'app/shared/model/departments.model';
import { IArea } from 'app/shared/model/area.model';
import { ITaskType } from 'app/shared/model/task-type.model';
import { IWorkNotes } from 'app/shared/model/work-notes.model';

export interface ITask {
  id?: number;
  description?: string | null;
  dueDate?: string | null;
  estimatedTimeToComplete?: number | null;
  estimatedTimeToCompleteTimeUnit?: string | null;
  isReadByAssignTo?: boolean | null;
  isUrgent?: boolean | null;
  isRejected?: boolean | null;
  isCompleted?: boolean | null;
  completedOn?: string | null;
  rejectedOn?: string | null;
  createOn?: string | null;
  updatedOn?: string | null;
  assignTo?: IPersons | null;
  department?: IDepartments | null;
  area?: IArea | null;
  type?: ITaskType | null;
  workNotes?: IWorkNotes[] | null;
}

export const defaultValue: Readonly<ITask> = {
  isReadByAssignTo: false,
  isUrgent: false,
  isRejected: false,
  isCompleted: false,
};
