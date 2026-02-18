package cs2110;

import static cs2110.WebsiteData.BY_TIMESTAMP;
import static cs2110.WebsiteData.DeduplicationPolicy.KEEP_ALL;
import static cs2110.WebsiteData.DeduplicationPolicy.KEEP_FIRST;
import static org.junit.jupiter.api.Assertions.*;
import static cs2110.WebsiteUtilities.*;

import cs2110.WebsiteData.VisitRecord;
import java.time.LocalDateTime;
import java.util.Comparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WebsiteUtilitiesTests {
    /* Note: These tests are meant to serve as basic correctness checks and examples
     * of how to set up unit tests for these methods. They do NOT come close to offering
     * good coverage of the `WebsiteUtilities` class. We encourage you to do additional
     * testing to gain confidence in the correctness of your submission.
     */

    @DisplayName("WHEN one of the `visits` records has the target timestamp, THEN the "
            + "`lowerBoundTimestamp()` method returns the index of that record.")
    @Test
    public void testLowerBoundFindsUniqueMatch() {
        VisitRecord[] visits = new VisitRecord[]{
            new VisitRecord("a", LocalDateTime.of(2026,1,1,0,0)),
            new VisitRecord("b", LocalDateTime.of(2026,1,2,0,0)),
            new VisitRecord("c", LocalDateTime.of(2026,1,3,0,0)),
            new VisitRecord("d", LocalDateTime.of(2026,1,4,0,0)),
            new VisitRecord("e", LocalDateTime.of(2026,1,5,0,0)),
            new VisitRecord("f", LocalDateTime.of(2026,1,6,0,0)),
            new VisitRecord("g", LocalDateTime.of(2026,1,7,0,0)),
        };
        assertEquals(2, lowerBoundTimestamp(visits, LocalDateTime.of(2026,1,3,0,0)));
    }

    @DisplayName("WHEN none of the `visits` records has the target timestamp, THEN the "
            + "`upperBoundTimestamp()` method returns the index of the first record that "
            + "is strictly later")
    @Test
    public void testUpperBoundNotPresent() {
        VisitRecord[] visits = new VisitRecord[]{
                new VisitRecord("a", LocalDateTime.of(2026,1,1,0,0)),
                new VisitRecord("b", LocalDateTime.of(2026,1,2,0,0)),
                new VisitRecord("c", LocalDateTime.of(2026,1,3,0,0)),
                new VisitRecord("d", LocalDateTime.of(2026,1,4,0,0)),
                new VisitRecord("e", LocalDateTime.of(2026,1,6,0,0)),
                new VisitRecord("f", LocalDateTime.of(2026,1,7,0,0)),
                new VisitRecord("g", LocalDateTime.of(2026,1,8,0,0)),
        };
        assertEquals(4, upperBoundTimestamp(visits, LocalDateTime.of(2026,1,5,0,0)));
    }

    /**
     * Asserts that `visits[l..r)` is sorted according to `cmp`
     */
    @SuppressWarnings("SameParameterValue")
    void assertSorted(VisitRecord[] visits, int l, int r, Comparator<VisitRecord> cmp) {
        for (int i = l; i < r - 1; i++) {
            assertTrue(cmp.compare(visits[i], visits[i + 1]) <= 0);
        }
    }

    @DisplayName("WHEN we merge on timestamps using the KEEP_ALL deduplication policy AND the "
            + "records are interleaved between the subarrays and have unique timestamps, THEN the "
            + "merged subarray is correctly sorted.")
    @Test
    void testMergeInterleavedUnique() {
        VisitRecord[] visits = new VisitRecord[]{
                new VisitRecord("a", LocalDateTime.of(2025, 1, 1, 0, 0)),
                new VisitRecord("b", LocalDateTime.of(2025, 1, 4, 0, 0)),
                new VisitRecord("c", LocalDateTime.of(2025, 1, 6, 0, 0)),
                new VisitRecord("d", LocalDateTime.of(2025, 1, 2, 0, 0)),
                new VisitRecord("e", LocalDateTime.of(2025, 1, 3, 0, 0)),
                new VisitRecord("f", LocalDateTime.of(2025, 1, 5, 0, 0)),
                new VisitRecord("g", LocalDateTime.of(2025, 1, 7, 0, 0)),
        };
        VisitRecord[] work = new VisitRecord[3];
        merge(visits, work, 0, 3, 3, 7, BY_TIMESTAMP, KEEP_ALL);
        assertSorted(visits, 0, 7, BY_TIMESTAMP);
    }

    @DisplayName("WHEN we call `deduplicatingSort()` with KEEP_FIRST on two equivalent and one "
            + "distinct records, THEN the output contains the correct two elements in the correct "
            + "order.")
    @Test
    void testSortThreeEquivalentPairKeepFirst() {
        VisitRecord[] visits = new VisitRecord[]{
                new VisitRecord("a", LocalDateTime.of(2026,1,2,0,0)),
                new VisitRecord("b", LocalDateTime.of(2026,1,1,0,0)),
                new VisitRecord("c", LocalDateTime.of(2026,1,1,0,0)),
        };
        VisitRecord[] sorted = deduplicatingSort(visits, BY_TIMESTAMP, KEEP_FIRST);
        assertEquals(2, sorted.length);
        assertEquals("b", sorted[0].userID());
        assertEquals("a", sorted[1].userID());
    }
}
