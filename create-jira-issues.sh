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


    response=$(curl -s -w "%{http_code}" -o response.json -X POST \
      -H "Authorization: Basic $JIRA_AUTH" \
      -H "Content-Type: application/json" \
      --data "{
        \"fields\": {
          \"project\": { \"key\": \"$PROJECT_KEY\" },
          \"summary\": \"$summary\",
          \"description\": \"$description\",
          \"issuetype\": { \"name\": \"$ISSUE_TYPE\" },
          \"labels\": [\"$LABEL\"],
          \"assignee\": { \"accountId\": \"$QA_ACCOUNT_ID\" }
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
