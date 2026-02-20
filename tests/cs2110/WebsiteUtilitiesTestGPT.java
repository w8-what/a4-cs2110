package cs2110;

import cs2110.WebsiteData.VisitRecord;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Comparator;

import static cs2110.WebsiteData.BY_TIMESTAMP;
import static cs2110.WebsiteData.DeduplicationPolicy.*;
import static org.junit.jupiter.api.Assertions.*;

public class WebsiteUtilitiesTestGPT {

    // ---------- helpers ----------

    private VisitRecord v(int t) {
        return new VisitRecord("u"+t, LocalDateTime.of(2020,1,1,0,t));
    }

    private VisitRecord[] arr(int... times) {
        VisitRecord[] a = new VisitRecord[times.length];
        for (int i = 0; i < times.length; i++) a[i] = v(times[i]);
        return a;
    }

    // ---------- lowerBound ----------

    @Test
    void lowerBound_basic() {
        VisitRecord[] a = arr(1,2,3,4,5);
        assertEquals(0, WebsiteUtilities.lowerBoundTimestamp(a, v(0).timestamp()));
        assertEquals(2, WebsiteUtilities.lowerBoundTimestamp(a, v(3).timestamp()));
        assertEquals(5, WebsiteUtilities.lowerBoundTimestamp(a, v(10).timestamp()));
    }

    @Test
    void lowerBound_duplicates() {
        VisitRecord[] a = arr(1,2,2,2,5);
        assertEquals(1, WebsiteUtilities.lowerBoundTimestamp(a, v(2).timestamp()));
    }

    @Test
    void lowerBound_singleElement() {
        VisitRecord[] a = arr(5);
        assertEquals(0, WebsiteUtilities.lowerBoundTimestamp(a, v(1).timestamp()));
    }

    // ---------- upperBound ----------

    @Test
    void upperBound_basic() {
        VisitRecord[] a = arr(1,2,3,4,5);
        assertEquals(0, WebsiteUtilities.upperBoundTimestamp(a, v(0).timestamp()));
        assertEquals(3, WebsiteUtilities.upperBoundTimestamp(a, v(3).timestamp()));
        assertEquals(5, WebsiteUtilities.upperBoundTimestamp(a, v(9).timestamp()));
    }

    @Test
    void upperBound_duplicates() {
        VisitRecord[] a = arr(1,2,2,2,5);
        assertEquals(4, WebsiteUtilities.upperBoundTimestamp(a, v(2).timestamp()));
    }

    @Test
    void upperBound_single() {
        VisitRecord[] a = arr(5);
        assertEquals(1, WebsiteUtilities.upperBoundTimestamp(a, v(5).timestamp()));
    }

    // ---------- deduplicatingSort KEEP_ALL ----------

    @Test
    void sort_keepAll_preservesDuplicates() {
        VisitRecord[] a = arr(3,1,2,2,1);
        VisitRecord[] sorted =
                WebsiteUtilities.deduplicatingSort(a, BY_TIMESTAMP, KEEP_ALL);

        assertEquals(5, sorted.length);
        assertEquals(1, sorted[0].timestamp().getMinute());
        assertEquals(1, sorted[1].timestamp().getMinute());
        assertEquals(2, sorted[2].timestamp().getMinute());
        assertEquals(2, sorted[3].timestamp().getMinute());
        assertEquals(3, sorted[4].timestamp().getMinute());
    }

    // ---------- KEEP_FIRST ----------

    @Test
    void sort_keepFirst() {
        VisitRecord[] a = arr(3,1,2,2,1);

        VisitRecord[] sorted =
                WebsiteUtilities.deduplicatingSort(a, BY_TIMESTAMP, KEEP_FIRST);

        assertEquals(3, sorted.length);

        assertEquals(1, sorted[0].timestamp().getMinute());
        assertEquals(2, sorted[1].timestamp().getMinute());
        assertEquals(3, sorted[2].timestamp().getMinute());
    }

    // ---------- KEEP_LAST ----------

    @Test
    void sort_keepLast() {
        VisitRecord[] a = arr(3,1,2,2,1);

        VisitRecord[] sorted =
                WebsiteUtilities.deduplicatingSort(a, BY_TIMESTAMP, KEEP_LAST);

        assertEquals(3, sorted.length);

        assertEquals(1, sorted[0].timestamp().getMinute());
        assertEquals(2, sorted[1].timestamp().getMinute());
        assertEquals(3, sorted[2].timestamp().getMinute());
    }

    // ---------- stability test ----------

    @Test
    void keepAll_isStable() {
        VisitRecord a = new VisitRecord("A", LocalDateTime.of(2020,1,1,0,5));
        VisitRecord b = new VisitRecord("B", LocalDateTime.of(2020,1,1,0,5));

        VisitRecord[] input = {a, b};

        VisitRecord[] out =
                WebsiteUtilities.deduplicatingSort(input, BY_TIMESTAMP, KEEP_ALL);

        assertSame(a, out[0]);
        assertSame(b, out[1]);
    }

    // ---------- empty + edge ----------

    @Test
    void emptyArray() {
        VisitRecord[] a = new VisitRecord[0];

        VisitRecord[] out =
                WebsiteUtilities.deduplicatingSort(a, BY_TIMESTAMP, KEEP_ALL);

        assertEquals(0, out.length);
    }

    @Test
    void singleElementSort() {
        VisitRecord[] a = arr(7);

        VisitRecord[] out =
                WebsiteUtilities.deduplicatingSort(a, BY_TIMESTAMP, KEEP_FIRST);

        assertEquals(1, out.length);
        assertEquals(7, out[0].timestamp().getMinute());
    }

    // ---------- custom comparator ----------

    @Test
    void customComparator_reverseOrder() {
        Comparator<VisitRecord> reverse =
                (x,y)-> BY_TIMESTAMP.compare(y,x);

        VisitRecord[] a = arr(1,3,2);

        VisitRecord[] out =
                WebsiteUtilities.deduplicatingSort(a, reverse, KEEP_ALL);

        assertEquals(3, out[0].timestamp().getMinute());
        assertEquals(2, out[1].timestamp().getMinute());
        assertEquals(1, out[2].timestamp().getMinute());
    }

}