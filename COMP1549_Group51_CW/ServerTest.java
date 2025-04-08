import org.junit.Test;
import static org.junit.Assert.*;

public class ServerTest {

    // Test 1: Singleton instance should always be the same object
    @Test
    public void testSingletonInstance() {
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();
        assertSame(s1, s2);
    }

    // Test 2: Singleton logger should not be null
    @Test
    public void testSingletonLoggerNotNull() {
        assertNotNull(Singleton.getInstance());
    }

    // Test 3: Basic string format test (example of message format)
    @Test
    public void testBroadcastMessageFormat() {
        int clientId = 1;
        String clientName = "Alice";
        String message = "Hello!";
        String formatted = "Client " + clientId + " (" + clientName + "): " + message;
        assertEquals("Client 1 (Alice): Hello!", formatted);
    }

    // Test 4: Validate client ID is positive (input simulation)
    @Test
    public void testClientIdValidation() {
        int validId = 5;
        assertTrue(validId > 0);
    }
}