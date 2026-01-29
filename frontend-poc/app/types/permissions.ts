export type Role = "ADMIN" | "ECONOMY" | "HR";

export interface CompanyRoles {
  companyId: string;
  roles: Role[];
}

export interface UserRoles {
  userId: string;
  companies: CompanyRoles[];
}
