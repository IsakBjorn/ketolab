import type { Route } from "./+types/home";
import { Form, useNavigate } from "react-router";
import { useState } from "react";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Permission Lookup" },
    { name: "description", content: "Look up user permissions" },
  ];
}

export default function Home() {
  const navigate = useNavigate();
  const [userId, setUserId] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (userId.trim()) {
      navigate(`/user/${encodeURIComponent(userId.trim())}`);
    }
  };

  return (
    <main className="flex items-center justify-center pt-16 pb-4">
      <div className="flex-1 flex flex-col items-center gap-8 min-h-0">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-gray-100">
          Permission Lookup
        </h1>
        <form onSubmit={handleSubmit} className="flex flex-col gap-4 w-full max-w-sm px-4">
          <label className="flex flex-col gap-2">
            <span className="text-gray-700 dark:text-gray-200">User ID</span>
            <input
              type="text"
              value={userId}
              onChange={(e) => setUserId(e.target.value)}
              placeholder="Enter user ID"
              className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-gray-800 dark:border-gray-600 dark:text-gray-100"
            />
          </label>
          <button
            type="submit"
            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50"
            disabled={!userId.trim()}
          >
            Look up permissions
          </button>
        </form>
      </div>
    </main>
  );
}
