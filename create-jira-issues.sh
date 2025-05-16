#!/bin/bash

JIRA_URL="https://strappcorp.atlassian.net"
JIRA_EMAIL="ngonzalez@strappcorp.com"
JIRA_TOKEN="$JIRA_TOKEN"
PROJECT_KEY="SMAT"
ISSUE_TYPE="Bug"
LABEL="auto-test-failed"

echo "🔍 Analizando pruebas fallidas de Allure..."

for file in allure-results/*.json; do
  if jq -e '.status == "failed"' "$file" > /dev/null; then
    name=$(jq -r '.name' "$file")
    message=$(jq -r '.statusDetails.message // "Sin mensaje de error."' "$file")
    suite=$(jq -r '.labels[] | select(.name == "suite") | .value' "$file")
    
    summary="Fallo en test: $name"
    description="Suite: $suite\n\nMensaje de error:\n$message\n\n[Ver ejecución](${BUILD_URL}allure)"

    echo "📌 Creando issue para test: $name"

    curl -s -X POST \
      -H "Authorization: Basic $(echo -n "$JIRA_EMAIL:$JIRA_TOKEN" | base64)" \
      -H "Content-Type: application/json" \
      --data "{
        \"fields\": {
          \"project\": { \"key\": \"$PROJECT_KEY\" },
          \"summary\": \"$summary\",
          \"description\": \"$description\",
          \"issuetype\": { \"name\": \"$ISSUE_TYPE\" },
          \"labels\": [\"$LABEL\"]
        }
      }" \
      "$JIRA_URL/rest/api/3/issue" > /dev/null

    echo "✅ Ticket creado: $summary"
  fi
done
