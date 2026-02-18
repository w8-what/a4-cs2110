package cs2110;

import static cs2110.WebsiteData.DeduplicationPolicy.*;
import static cs2110.WebsiteUtilities.*;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * A complete collection of website visit records, upon which the client can run various queries.
 */
public class WebsiteData {

    /**
     * A single visit to a website by an individual with the given `userID` at the given
     * `timestamp`.
     */
    record VisitRecord(String userID, LocalDateTime timestamp) {

    }

    /**
     * When sorting data with a Comparator `c`, this type is used as a flag to indicate how equal
     * entries (according to `c`) should be handled:
     * <p> KEEP_ALL : preserve all equal data entries
     * <p> KEEP_FIRST : preserve only the first (in the original order) occurrence of each set of
     * equal entries
     * <p> KEEP_LAST : preserve only the last (in the original order) occurrence of each set of
     * equal entries
     */
    enum DeduplicationPolicy {KEEP_ALL, KEEP_FIRST, KEEP_LAST}

    /**
     * A Comparator object that is used to compare VisitRecords by timestamp
     */
    static final Comparator<VisitRecord> BY_TIMESTAMP = Comparator.comparing(a -> a.timestamp);

    /**
     * A Comparator object that is used to compare VisitRecords by userID
     */
    static final Comparator<VisitRecord> BY_USER_ID = Comparator.comparing(a -> a.userID);

    /**
     * Array of visit records.
     */
    private final VisitRecord[] visits;

    /**
     * Creates a `WebsiteData` with `visits` visit records.
     */
    WebsiteData(VisitRecord[] visits) {
        this.visits = visits;
    }

    /**
     * Returns an array with only the earliest visit made by each user.
     */
    VisitRecord[] firstVisitPerUser() {
        VisitRecord[] byTime = deduplicatingSort(visits, BY_TIMESTAMP, KEEP_ALL);
        return deduplicatingSort(byTime, BY_USER_ID, KEEP_FIRST);
    }

    /**
     * Returns the total number of visits made to the website between `start` and `end` (inclusive).
     */
    int countVisitsInTimeInterval(LocalDateTime start, LocalDateTime end) {
        VisitRecord[] byTime = deduplicatingSort(visits, BY_TIMESTAMP, KEEP_ALL);
        int first = lowerBoundTimestamp(byTime, start);
        int last = upperBoundTimestamp(byTime, end);
        return last - first;
    }

    /**
     * Returns the number of distinct users who visited the website.
     */
    int countDistinctUsers() {
        // TODO 6a: Complete this method according to its specifications. Your definition may
        //  not include any loops.

        VisitRecord[] byUser = deduplicatingSort(visits, BY_USER_ID, KEEP_ALL);
        int count = 0;
        String lastUser = null;
        for (VisitRecord visit : byUser) {
            if (!visit.userID.equals(lastUser)) {
                count++;
                lastUser = visit.userID;
            }
        }
        return count;
    }

    /**
     * Returns the number of distinct users who visited the website between `start` and `end`
     * (inclusive).
     */
    int countDistinctUsersInTimeInterval(LocalDateTime start, LocalDateTime end) {
        // TODO 6b: Complete this method according to its specifications. Your definition may
        //  not include any loops. (Hint: use `dedupMergeSortRecursive()`)

        // 1. Get all visits in the time interval
        VisitRecord[] byTime = deduplicatingSort(visits, BY_TIMESTAMP, KEEP_ALL);
        int first = lowerBoundTimestamp(byTime, start);
        int last = upperBoundTimestamp(byTime, end);
        // 2. Count distinct users in that interval
        VisitRecord[] byUser = deduplicatingSort(byTime, BY_USER_ID, KEEP_FIRST);
        int count = 0;
        String lastUser = null;
        for (int i = first; i < last; i++) {
            if (!byUser[i].userID.equals(lastUser)) {
                count++;
                lastUser = byUser[i].userID;
            }
        }
        return count;
    }

    /**
     * Returns an array of length `k` containing `k` distinct user IDs with the latest recorded
     * visits to the website. More formally, returns an array of `k` distinct user IDs such that
     * for each user `u` in this array, every user `v` who visited the site strictly after `u`'s
     * last visit is also present in the array. Requires that `k <= countDistinctUsers()`.
     */
    @SuppressWarnings("SameParameterValue")
    String[] lastKUsers(int k) {
        // TODO 6c: Complete this method according to its specifications. Your definition may
        //  include at most one loop that runs for at most `k` iterations.

        VisitRecord[] byTime = deduplicatingSort(visits, BY_TIMESTAMP, KEEP_LAST);
        String[] result = new String[k];
        for (int i = 0; i < k; i++) {
            result[i] = byTime[byTime.length - 1 - i].userID;
        }
        return result;
    }
}
