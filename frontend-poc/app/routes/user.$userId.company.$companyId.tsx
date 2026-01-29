import type { Route } from "./+types/user.$userId.company.$companyId";
import { Link, useLoaderData } from "react-router";
import type { CompanyRoles, UserRoles } from "../types/permissions";

interface LoaderData {
  userId: string;
  companyId: string;
  roles: string[];
}

export async function loader({ params }: Route.LoaderArgs): Promise<LoaderData> {
  const response = await fetch(
    `http://localhost:8080/api/permissions/user/${encodeURIComponent(params.userId)}`
  );
  if (!response.ok) {
    throw new Response("Failed to fetch user permissions", { status: response.status });
  }

  const userRoles: UserRoles = await response.json();
  const company = userRoles.companies.find(
    (c) => c.companyId === params.companyId
  );

  return {
    userId: params.userId,
    companyId: params.companyId,
    roles: company?.roles ?? [],
  };
}

export function meta({ params }: Route.MetaArgs) {
  return [
    { title: `Roles for ${params.companyId}` },
    { name: "description", content: "Company roles" },
  ];
}

export default function CompanyRolesPage() {
  const { userId, companyId, roles } = useLoaderData<typeof loader>();

  return (
    <main className="flex items-center justify-center pt-16 pb-4">
      <div className="flex-1 flex flex-col items-center gap-8 min-h-0">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-gray-100">
          Roles for {companyId}
        </h1>
        <p className="text-gray-600 dark:text-gray-400">User: {userId}</p>

        {roles.length === 0 ? (
          <p className="text-gray-600 dark:text-gray-400">No roles found</p>
        ) : (
          <ul className="flex flex-col gap-2 w-full max-w-sm px-4">
            {roles.map((role) => (
              <li
                key={role}
                className="px-4 py-3 bg-gray-100 dark:bg-gray-800 rounded-lg text-gray-900 dark:text-gray-100"
              >
                {role}
              </li>
            ))}
          </ul>
        )}

        <Link
          to={`/user/${encodeURIComponent(userId)}`}
          className="text-blue-600 dark:text-blue-400 hover:underline"
        >
          Back to company list
        </Link>
      </div>
    </main>
  );
}
