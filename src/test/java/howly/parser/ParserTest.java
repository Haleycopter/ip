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
}
