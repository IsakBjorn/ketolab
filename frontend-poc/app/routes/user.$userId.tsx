import type { Route } from "./+types/user.$userId";
import { Link, useLoaderData } from "react-router";
import type { UserRoles } from "../types/permissions";

export async function loader({ params }: Route.LoaderArgs): Promise<UserRoles> {
  const response = await fetch(
    `http://localhost:8080/api/permissions/user/${encodeURIComponent(params.userId)}`
  );
  if (!response.ok) {
    throw new Response("Failed to fetch user permissions", { status: response.status });
  }
  return response.json();
}

export function meta({ params }: Route.MetaArgs) {
  return [
    { title: `Companies for ${params.userId}` },
    { name: "description", content: "User company list" },
  ];
}

export default function UserCompanies() {
  const userRoles = useLoaderData<typeof loader>();

  return (
    <main className="flex items-center justify-center pt-16 pb-4">
      <div className="flex-1 flex flex-col items-center gap-8 min-h-0">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-gray-100">
          Companies for {userRoles.userId}
        </h1>

        {userRoles.companies.length === 0 ? (
          <p className="text-gray-600 dark:text-gray-400">No companies found</p>
        ) : (
          <ul className="flex flex-col gap-2 w-full max-w-sm px-4">
            {userRoles.companies.map((company) => (
              <li key={company.companyId}>
                <Link
                  to={`/user/${encodeURIComponent(userRoles.userId)}/company/${encodeURIComponent(company.companyId)}`}
                  className="block px-4 py-3 bg-gray-100 dark:bg-gray-800 rounded-lg hover:bg-gray-200 dark:hover:bg-gray-700 text-blue-600 dark:text-blue-400"
                >
                  {company.companyId}
                </Link>
              </li>
            ))}
          </ul>
        )}

        <Link
          to="/"
          className="text-blue-600 dark:text-blue-400 hover:underline"
        >
          Back to search
        </Link>
      </div>
    </main>
  );
}
