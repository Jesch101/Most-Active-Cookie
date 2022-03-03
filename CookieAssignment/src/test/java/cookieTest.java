import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class cookieTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    // When cmd line arguments are valid, the given date should output three active cookies
    @Test
    void Filter_three_active_cookies(){
        most_active_cookie.main(new String[]{"cookie_log.csv", "-d", "2018-12-08"});
        assertEquals("fbcn5UAVanZf6UtG\nSAZuXPGUrfbcn5UA\n4sMM2LxV07bPJzwf", outputStreamCaptor.toString().trim());
    }

    // When cmd line arguments are valid, the given date should output the most active cookie
    @Test
    void Filter_single_active_cookie(){
        most_active_cookie.main(new String[]{"cookie_log.csv", "-d", "2018-12-09"});
        assertEquals("AtY0laUfhglK3lC7", outputStreamCaptor.toString().trim());
    }

    // When given an invalid date in cmd line arguments, the output should be an invalid date error
    @Test
    void Invalid_date(){
        most_active_cookie.main(new String[]{"cookie_log.csv", "-d", "2018-12"});
        assertTrue(outputStreamCaptor.toString().trim().contains("Invalid date. Format should be (yyyy-MM-dd)"));
    }

    // When given an invalid flag in cmd line options, the output should be an unrecognized option
    @Test
    void CLI_flag_not_in_options(){
        most_active_cookie.main(new String[]{"cookie_log.csv", "-f", "2018-12-08"});
        assertEquals("Unrecognized option: -f", outputStreamCaptor.toString().trim());
    }

    // When given an invalid file in cmd line arguments, a file does not exist error should be outputted
    @Test
    void Invalid_file_name(){
        most_active_cookie.main(new String[]{"dummy_log.csv", "-d", "2018-12-08"});
        assertEquals("Error: File does not exist", outputStreamCaptor.toString().trim());
    }

    // Given date has no active cookies
    @Test
    void No_active_cookies(){
        most_active_cookie.main(new String[]{"cookie_log.csv", "-d", "2018-12-10"});
        assertEquals("No active cookies", outputStreamCaptor.toString().trim());
    }




}