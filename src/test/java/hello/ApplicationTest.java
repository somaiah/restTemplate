package hello;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ApplicationTest {

    private Application application;

    @Before
    public void setUp() {
        application = new Application();
    }

    @Test
    public void testGetIssue() {
        ResponseEntity issueResponse = application.getIssue("MOON-13");

        assertThat(issueResponse.getStatusCode().value(), is(200));
        assertThat(issueResponse.getBody().toString(), containsString("\"key\":\"MOON-13\""));
    }

    @Test
    public void testCreateIssue() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ResponseEntity issueResponse = application.createIssue("MOON", "This is a test", localDateTime.toString(), "Bug");

        assertThat(issueResponse.getStatusCode().value(), is(201));
        assertThat(issueResponse.getBody().toString(), containsString("\"key\":\"MOON-"));
    }

}
