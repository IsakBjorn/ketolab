import { type RouteConfig, index, route } from "@react-router/dev/routes";

export default [
  index("routes/home.tsx"),
  route("user/:userId", "routes/user.$userId.tsx"),
  route("user/:userId/company/:companyId", "routes/user.$userId.company.$companyId.tsx"),
] satisfies RouteConfig;
