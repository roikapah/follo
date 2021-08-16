import dayjs from 'dayjs';
import { IDepartments } from 'app/shared/model/departments.model';
import { Role } from 'app/shared/model/enumerations/role.model';

export interface IPersons {
  id?: number;
  name?: string | null;
  email?: string;
  role?: Role;
  phoneNumber?: string;
  address?: string | null;
  createOn?: string | null;
  updatedOn?: string | null;
  department?: IDepartments | null;
}

export const defaultValue: Readonly<IPersons> = {};
