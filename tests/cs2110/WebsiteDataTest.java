package cs2110;

import static org.junit.jupiter.api.Assertions.*;
import static cs2110.WebsiteData.*;
import static cs2110.Simulation.*;
import static cs2110.WebsiteData.DeduplicationPolicy.*;

import cs2110.WebsiteData.VisitRecord;
import java.io.IOException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WebsiteDataTest {

    /* Note: These tests are meant to serve as basic correctness checks and examples
     * of how to set up unit tests for these methods. They do NOT come close to offering
     * good coverage of the `WebsiteData` class. We encourage you to do additional
     * testing to gain confidence in the correctness of your submission.
     */

    @DisplayName("The `Simulation.fromFile()` method correctly parses "
            + "the contents of a file to a `VisitRecord` array.")
    @Test
    void testFromFile() throws IOException {
        VisitRecord[] records = fromFile("small.txt");
        assertEquals(20, records.length); // correct number of entries

        VisitRecord[] expected = new VisitRecord[] {
            new VisitRecord("userA", LocalDateTime.of(2026, 1, 1, 5, 5)),
            new VisitRecord("userA", LocalDateTime.of(2026, 1, 1, 0, 10)),
            new VisitRecord("userA", LocalDateTime.of(2026, 1, 1, 1, 5)),
            new VisitRecord("userC", LocalDateTime.of(2026, 1, 1, 0, 15)),
            new VisitRecord("userD", LocalDateTime.of(2026, 1, 1, 5, 10)),
            new VisitRecord("userC", LocalDateTime.of(2026, 1, 1, 0, 5)),
            new VisitRecord("userC", LocalDateTime.of(2026, 1, 1, 5, 10)),
            new VisitRecord("userD", LocalDateTime.of(2026, 1, 1, 0, 0)),
            new VisitRecord("userB", LocalDateTime.of(2026, 1, 1, 5, 0)),
            new VisitRecord("userA", LocalDateTime.of(2026, 1, 1, 5, 0)),
            new VisitRecord("userB", LocalDateTime.of(2026, 1, 1, 1, 10)),
            new VisitRecord("userB", LocalDateTime.of(2026, 1, 1, 1, 5)),
            new VisitRecord("userA", LocalDateTime.of(2026, 1, 1, 3, 0)),
            new VisitRecord("userA", LocalDateTime.of(2026, 1, 1, 0, 15)),
            new VisitRecord("userC", LocalDateTime.of(2026, 1, 1, 0, 5)),
            new VisitRecord("userD", LocalDateTime.of(2026, 1, 1, 2, 10)),
            new VisitRecord("userD", LocalDateTime.of(2026, 1, 1, 1, 15)),
            new VisitRecord("userA", LocalDateTime.of(2026, 1, 1, 3, 10)),
            new VisitRecord("userA", LocalDateTime.of(2026, 1, 1, 2, 10)),
            new VisitRecord("userA", LocalDateTime.of(2026, 1, 1, 4, 5))
        };

        assertArrayEquals(expected, records);
    }

    @DisplayName("WHEN we call the `firstVisitPerUser()` on the released `small.txt` data, it "
            + "returns the VisitRecords for userA, userB, userC, and userD with the earliest "
            + "timestamps.")
    @Test
    void testFirstVisitSmall() throws IOException {
        WebsiteData data = new WebsiteData(fromFile("small.txt"));
        VisitRecord[] earliest = data.firstVisitPerUser();
        assertEquals(4, earliest.length); // correct number of entries

        // Note: Order of these elements is underspecified; sort to remove ambiguity
        earliest = WebsiteUtilities.deduplicatingSort(earliest, BY_USER_ID, KEEP_ALL);
        assertEquals("userA", earliest[0].userID());
        assertEquals(LocalDateTime.of(2026, 1, 1, 0, 10), earliest[0].timestamp());
        assertEquals("userB", earliest[1].userID());
        assertEquals(LocalDateTime.of(2026, 1, 1, 1, 5), earliest[1].timestamp());
        assertEquals("userC", earliest[2].userID());
        assertEquals(LocalDateTime.of(2026, 1, 1, 0, 5), earliest[2].timestamp());
        assertEquals("userD", earliest[3].userID());
        assertEquals(LocalDateTime.of(2026, 1, 1, 0, 0), earliest[3].timestamp());
    }

    @DisplayName("WHEN we call the `countVisitsInTimeInterval()` on the released `small.txt` data, "
            + "for a time interval within the range of the records, THEN it returns the correct "
            + "count.")
    @Test
    void testCountInIntervalSmall() throws IOException {
        WebsiteData data = new WebsiteData(fromFile("small.txt"));
        int count = data.countVisitsInTimeInterval(
            LocalDateTime.of(2026, 1, 1, 1, 45), // start
            LocalDateTime.of(2026, 1, 1, 4, 15)  // end
        );
        assertEquals(5, count);
    }

    @DisplayName("WHEN we call the `countDistinctUsers()` on the released `small.txt` data, THEN "
            + "it returns the correct count, 4.")
    @Test
    void testCountDistinctUsersSmall() throws IOException {
        WebsiteData data = new WebsiteData(fromFile("small.txt"));
        int count = data.countDistinctUsers();
        assertEquals(4, count);
    }

    @DisplayName("WHEN we call the `countDistinctUsersInTimeInterval()` on the released `small.txt` "
            + "data for a time interval within the range of the records, THEN it returns the "
            + "correct count, 2.")
    @Test
    void testCountDistinctUsersTimeIntervalSmall() throws IOException {
        WebsiteData data = new WebsiteData(fromFile("small.txt"));
        int count = data.countDistinctUsersInTimeInterval(
            LocalDateTime.of(2026, 1, 1, 1, 45), // start
            LocalDateTime.of(2026, 1, 1, 4, 15)  // end
        );
        assertEquals(2, count);
    }

    @DisplayName("WHEN we call the `lastKUsers()` on the released `small.txt` data with k=2, THEN "
            + "it returns an array containing the userIDs of the users with the 2 latest timestamps.")
    @Test
    void testLastKUsersSmall() throws IOException {
        WebsiteData data = new WebsiteData(fromFile("small.txt"));
        String[] userIDs = data.lastKUsers(2);
        assertEquals(2, userIDs.length);
        assertTrue(userIDs[0].equals("userC") || userIDs[1].equals("userC"));
        assertTrue(userIDs[0].equals("userD") || userIDs[1].equals("userD"));
    }

}
