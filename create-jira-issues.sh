#!/bin/bash

JIRA_URL="https://strappcorp.atlassian.net"
JIRA_EMAIL="ngonzalez@strappcorp.com"
JIRA_AUTH=$(echo -n "$JIRA_EMAIL:$JIRA_TOKEN" | base64)
PROJECT_KEY="SMAT"
ISSUE_TYPE="Bug"
LABEL="auto-test-failed"

echo "🔍 Analizando pruebas fallidas de Allure..."
echo "🔗 BUILD_URL: $BUILD_URL"
echo "📂 Archivos encontrados:"
ls -l allure-results/*.json || echo "No se encontraron archivos .json"

for file in allure-results/*.json; do
  if jq -e '.status == "failed"' "$file" > /dev/null; then
    name=$(jq -r '.name' "$file")
    message=$(jq -r '.statusDetails.message // "Sin mensaje de error."' "$file")
    suite=$(jq -r '.labels[] | select(.name == "suite") | .value' "$file")
    
    summary="Fallo en test: $name"
    description="Suite: $suite\n\nMensaje de error:\n$message\n\n[Ver ejecución](${BUILD_URL}allure)"

    echo "📌 Creando issue para test: $name"

    response=$(curl -s -w "%{http_code}" -o response.json -X POST \
      -H "Authorization: Basic $JIRA_AUTH" \
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
      "$JIRA_URL/rest/api/3/issue")

    echo "🔎 HTTP Status: $response"
    cat response.json
  fi
done
