import { Namespace, Context } from "@ory/keto-namespace-types"

class User implements Namespace {}

class Company implements Namespace {
  related: {
    admin: User[]
    hr: User[]
    economy: User[]
  }

  permits = {
    // Role checks
    isAdmin: (ctx: Context): boolean =>
      this.related.admin.includes(ctx.subject),

    isHr: (ctx: Context): boolean =>
      this.related.hr.includes(ctx.subject) ||
      this.permits.isAdmin(ctx),

    isEconomy: (ctx: Context): boolean =>
      this.related.economy.includes(ctx.subject) ||
      this.permits.isAdmin(ctx),

    // HR tasks
    canListEmployees: (ctx: Context): boolean => this.permits.isHr(ctx),
    canViewEmployee: (ctx: Context): boolean => this.permits.isHr(ctx),
    canEditEmployee: (ctx: Context): boolean => this.permits.isHr(ctx),

    // Economy tasks
    canReadReports: (ctx: Context): boolean => this.permits.isEconomy(ctx),
    canCreateReport: (ctx: Context): boolean => this.permits.isEconomy(ctx),
  }
}
