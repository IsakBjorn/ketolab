import { Namespace, Context } from "@ory/keto-namespace-types"

class User implements Namespace {}

class Role implements Namespace {
  related: {
    member: User[]
    inherits: Role[]
  }

  permits = {
    hasRole: (ctx: Context): boolean =>
      this.related.member.includes(ctx.subject) ||
      this.related.inherits.traverse((role) => role.permits.hasRole(ctx))
  }
}
