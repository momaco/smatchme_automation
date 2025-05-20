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
echo "$JIRA_TOKEN"
created_count=0

for file in allure-results/*result*.json; do
  status=$(jq -r '.status // empty' "$file")
  echo "📄 Revisando archivo: $file (status: $status)"

 if [ "$status" = "failed" ] || [ "$status" = "broken" ]; then

    name=$(jq -r '.name' "$file")
    message=$(jq -r '.statusDetails.message // "Sin mensaje de error."' "$file")
    echo "Mensaje : $message"   

    summary="❌ Test fallido: $name"
    description="Se detectó una falla automática:\n\n🧪 Test: $name\n💬 Detalles: $message\n🔗 Build: $BUILD_URL"
    echo "Descripcion : $description"

    response=$(curl -s -w "%{http_code}" -o response.json -X POST \
      -H "Authorization: Basic $JIRA_AUTH" \
      -H "Content-Type: application/json" \
      --data "{
        \"fields\": {
          \"project\": { \"key\": \"$PROJECT_KEY\" },
          \"summary\": \"$summary\",
          \"issuetype\": {\"name\": \"Bug\", \"id\": \"10006\" },
          \"labels\": [\"$LABEL\"],
          \"reporter\": { \"id\": \"712020:c070dc6e-c712-473e-8870-93aac1c5fd46\" },
          \"description\": {
            \"content\": [{
              \"content\": [{\"text\": \"$description\", \"type\": \"text\"}],
              \"type\": \"paragraph\"
            }],
            \"type\": \"doc\",
            \"version\": 1
          }
        }
      }" "$JIRA_URL/rest/api/3/issue")

    if [ "$response" = "201" ]; then
      ticket_key=$(jq -r '.key' response.json)
      echo "✅ Ticket creado: $ticket_key"
      created_count=$((created_count + 1))
    else
      echo "❌ Error al crear ticket para: $name (HTTP $response)"
      cat response.json
    fi
  fi
done

echo "📈 Total de tickets creados: $created_count"

