#!/bin/bash
# Load relation tuples into Keto from JSON file

set -e

KETO_WRITE_URL="${KETO_WRITE_URL:-http://localhost:4467}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TUPLES_FILE="${1:-$SCRIPT_DIR/relation_tuples.json}"

echo "Loading relation tuples from: $TUPLES_FILE"
echo "Keto write URL: $KETO_WRITE_URL"

# Read the JSON array and create each tuple
jq -c '.[]' "$TUPLES_FILE" | while read -r tuple; do
  echo "Creating tuple: $tuple"
  curl -s -X PUT "$KETO_WRITE_URL/admin/relation-tuples" \
    -H "Content-Type: application/json" \
    -d "$tuple"
  echo ""
done

echo "Done loading tuples."
