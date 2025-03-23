import org.junit.Test;
import static org.junit.Assert.*;

public class ServerTest {

    @Test
    public void testSingletonInstance() {
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();
        assertSame(s1, s2);
    }
}
