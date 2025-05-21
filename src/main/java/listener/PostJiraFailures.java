package listener;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.*;

public class PostJiraFailures {

    private static final String JIRA_URL = "https://strappcorp.atlassian.net";
    private static final String JIRA_EMAIL = "ngonzalez@strappcorp.com";
    private static final String JIRA_TOKEN = System.getenv("JIRA_TOKEN");
    private static final String PROJECT_KEY = "SMAT";
    private static final String ISSUE_TYPE = "Bug";
    private static final String LABEL = "auto-test-failed";
    private static final String QA_ACCOUNT_ID = "712020:c070dc6e-c712-473e-8870-93aac1c5fd46";
    private static final String BUILD_URL = System.getenv("BUILD_URL");

    public static void main(String[] args) throws Exception {
        AtomicInteger createdCount = new AtomicInteger(0);


        String auth = Base64.getEncoder().encodeToString((JIRA_EMAIL + ":" + JIRA_TOKEN).getBytes());
        JSONObject myself = sendGet(JIRA_URL + "/rest/api/3/myself", auth);
        System.out.println("🔍 Usuario autenticado: " + myself.optString("displayName"));


        Files.list(Paths.get("allure-results"))
                .filter(path -> path.toString().contains("result") && path.toString().endsWith(".json"))
                .forEach(file -> {
                    try {
                        String content = Files.readString(file);
                        JSONObject test = new JSONObject(content);
                        String status = test.optString("status", "");

                        System.out.println("📄 Revisando: " + file + " (status: " + status + ")");
                        if ("failed".equals(status) || "broken".equals(status)) {
                            String name = test.optString("name");
                            if (name.matches("^(setUp|tearDown)$")) {
                                System.out.println("⚠️ Ignorando fixture: " + name);
                                return;
                            }
                            String message = test.optJSONObject("statusDetails") != null ?
                                    test.getJSONObject("statusDetails").optString("message", "Sin mensaje de error.") :
                                    "Sin mensaje de error.";
                            String description = "Se detectó una falla automática:\n\n🧪 Test: " + name +
                                    "\n💬 Detalles: " + message + "\n🔗 Build: " + BUILD_URL;

                            JSONObject issuePayload = buildPayload(name, description);
                            JSONObject issueResponse = sendPost(JIRA_URL + "/rest/api/3/issue", auth, issuePayload);
                            if (issueResponse.has("key")) {
                                System.out.println("✅ Ticket creado: " + issueResponse.getString("key"));
                                createdCount.incrementAndGet();
                            } else {
                                System.out.println("❌ Error al crear ticket para: " + name);
                                System.out.println(issueResponse.toString(2));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        System.out.println("📈 Total de tickets creados: " + createdCount.get());
    }

    private static JSONObject buildPayload(String name, String description) {
        JSONObject descContent = new JSONObject()
                .put("type", "paragraph")
                .put("content", new JSONArray().put(new JSONObject()
                        .put("type", "text")
                        .put("text", description)));

        JSONObject payload = new JSONObject();
        payload.put("fields", new JSONObject()
                .put("project", new JSONObject().put("key", PROJECT_KEY))
                .put("summary", "❌ Test fallido: " + name)
                .put("issuetype", new JSONObject().put("name", ISSUE_TYPE))
                .put("reporter", new JSONObject().put("id", QA_ACCOUNT_ID))
                .put("labels", new JSONArray().put(LABEL))
                .put("description", new JSONObject()
                        .put("type", "doc")
                        .put("version", 1)
                        .put("content", new JSONArray().put(descContent)))
        );
        return payload;
    }

    private static JSONObject sendGet(String urlStr, String auth) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Basic " + auth);
        conn.setRequestProperty("Accept", "application/json");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return new JSONObject(reader.lines().reduce("", (a, b) -> a + b));
        }
    }

    private static JSONObject sendPost(String urlStr, String auth, JSONObject payload) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Basic " + auth);
        conn.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.toString().getBytes());
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return new JSONObject(reader.lines().reduce("", (a, b) -> a + b));
        } catch (IOException err) {
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                return new JSONObject(errorReader.lines().reduce("", (a, b) -> a + b));
            }
        }
    }
}
