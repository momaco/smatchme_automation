package listener;

import org.testng.ISuite;
import org.testng.ISuiteListener;

public class JiraIssueListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        // Al inicio no hacemos nada
    }

    @Override
    public void onFinish(ISuite suite) {
        // Aquí puedes revisar tus resultados o recibir un reporte y crear issues en Jira
        System.out.println("Suite finalizada, ejecutando creación de tickets Jira...");

        try {
            PostJiraFailures.main(new String[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
