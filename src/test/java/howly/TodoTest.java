package howly;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import howly.tasks.ToDo;

public class TodoTest {
    @Test
    public void testStringConversion() {
        assertEquals("[T][ ] read book", new ToDo("read book").toString());
    }
}
