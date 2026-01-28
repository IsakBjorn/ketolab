import { Namespace, Context } from "@ory/keto-namespace-types"

class User implements Namespace {}

class Company implements Namespace {
  related: {
    admin: User[]
    hr: User[]
    economy: User[]
  }

  permits = {
    isAdmin: (ctx: Context): boolean =>
      this.related.admin.includes(ctx.subject),

    isHr: (ctx: Context): boolean =>
      this.related.hr.includes(ctx.subject) ||
      this.permits.isAdmin(ctx),

    isEconomy: (ctx: Context): boolean =>
      this.related.economy.includes(ctx.subject) ||
      this.permits.isAdmin(ctx)
  }
}
