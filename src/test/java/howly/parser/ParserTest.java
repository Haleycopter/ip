package howly.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import howly.common.HowlyException;

public class ParserTest {

    @Test
    public void parseEvent_invalidDateRange_throwsHowlyException() {
        try {
            // Testing the logic you just implemented in parseEvent
            Parser.parseEvent("event trip /from 2026-12-01 /to 2026-01-01");
            fail("Should have thrown a HowlyException for invalid date range");
        } catch (HowlyException e) {
            assertEquals("The start date (/from) cannot be after the end date (/to)!", e.getMessage());
        }
    }

    @Test
    public void parseDeadline_validInput_returnsCorrectDeadline() throws HowlyException {
        String input = "deadline Submit project /by 2026-02-20";
        String[] result = Parser.parseDeadline(input);
        assertEquals("Submit project", result[0]);
        assertEquals("2026-02-20", result[1]);
    }
}
