#!/bin/bash

JIRA_URL="https://strappcorp.atlassian.net"
JIRA_EMAIL="ngonzalez@strappcorp.com"
JIRA_AUTH=$(echo -n "$JIRA_EMAIL:$JIRA_TOKEN" | base64)
PROJECT_KEY="SMAT"
ISSUE_TYPE="Bug"
LABEL="auto-test-failed"
QA_ACCOUNT_ID="712020:c070dc6e-c712-473e-8870-93aac1c5fd46"

echo "🔍 Analizando pruebas fallidas de Allure..."
echo "🔗 BUILD_URL: $BUILD_URL"
created_count=0

for file in allure-results/*.json; do
  status=$(jq -r '.status // empty' "$file")

  if [ "$status" = "failed" ]; then
    name=$(jq -r '.name' "$file")
    message=$(jq -r '.statusDetails.message // "Sin mensaje de error."' "$file")

  summary = "❌ Test fallido: $name"
  description = "Se detectó una falla automática:\n\n🧪 Test: $name\n💬 Detalles: $message\n🔗 Build: $BUILD_URL"

    response=$(curl -s -w "%{http_code}" -o response.json -X POST \
      -H "Authorization: Basic $JIRA_AUTH" \
      -H "Content-Type: application/json" \
      --data "{
        \"fields\": {
          \"project\": { \"key\": \"$PROJECT_KEY\" },
          \"summary\": \"$summary\",
          \"issuetype\": { \"name\": \"$ISSUE_TYPE\", \"id\": \"10006\"},
          \"labels\": [\"$LABEL\"],
          \"reporter\": { \"id\": \"$QA_ACCOUNT_ID\" },
          \"description\": {\"content\": [{\"content\": [{\"text\": \"$description\",\"type\": \"text\"}],\"type\": \"paragraph\"}]}
          }
      }" \
      "$JIRA_URL/rest/api/3/issue")

    echo "🔎 HTTP Status: $response"
    cat response.json
     echo "✅ Ticket creado: $summary"
    created_count=$((created_count + 1))
  fi
done
echo "📈 Total de tickets creados: $created_count"
