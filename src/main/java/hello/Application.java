package hello;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

public class Application {

    private static final String username = "user@domain.com";
    private static final String password = "password";
    private static final String jiraBaseURL = "https://jira.domain.com/rest/api/2/";
    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;

    public Application() {
        restTemplate = new RestTemplate();
        httpHeaders = createHeadersWithAuthentication();
    }

    private HttpHeaders createHeadersWithAuthentication() {
        String plainCreds = username + ":" + password;
        byte[] base64CredsBytes = Base64.getEncoder().encode(plainCreds.getBytes());
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        return headers;
    }

    public ResponseEntity getIssue(String issueId) {
        String url = jiraBaseURL + "issue/" + issueId;

        HttpEntity<?> requestEntity = new HttpEntity(httpHeaders);
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
    }

    public ResponseEntity createIssue(String key, String summary, String description, String issueType) {
        String createIssueJSON = createCreateIssueJSON(key, summary, description, issueType);

        String url = jiraBaseURL + "issue";

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<String>(createIssueJSON, httpHeaders);

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

    }

    private String createCreateIssueJSON(String key, String summary, String description, String issueType) {
        String createIssueJSON = "{\"fields\":{\"project\":{\"key\":\"$KEY\"},\"summary\":\"$SUMMARY\",\"description\":\"$DESCRIPTION\",\"issuetype\": {\"name\": \"$ISSUETYPE\"}}}";

        createIssueJSON = createIssueJSON.replace("$KEY", key);
        createIssueJSON = createIssueJSON.replace("$SUMMARY", summary);
        createIssueJSON = createIssueJSON.replace("$DESCRIPTION", description);
        return createIssueJSON.replace("$ISSUETYPE", issueType);
    }

}