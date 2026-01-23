package howly;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import howly.parser.Parser;
import howly.common.HowlyException;

public class ParserTest {
    @Test
    public void parseTodo_emptyDescription_exceptionThrown() {
        try {
            Parser.parseTodo("todo ");
            fail(); // test should not reach this line
        } catch (HowlyException e) {
            assertEquals("You have to specify the task after 'todo'.", e.getMessage());
        }
    }
}