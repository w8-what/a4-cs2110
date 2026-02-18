package cs2110;

import cs2110.WebsiteData.VisitRecord;
import cs2110.WebsiteData.DeduplicationPolicy;
import java.time.LocalDateTime;
import java.util.Comparator;

import static cs2110.WebsiteData.BY_TIMESTAMP;
import static cs2110.WebsiteData.DeduplicationPolicy.*;

/**
 * Utilities for deduplicating, sorting, and searching `VisitRecord` data.
 */
public class WebsiteUtilities {

    /**
     * Returns the index `i` with `0 <= i <= visits.length` such that `visits[..i)` all occurred
     * strictly before timestamp `t` and `visits[i..]` all occurred at or after timestamp `t`. No
     * modifications are made to the array `visits` as a result of this method. Requires that
     * `visits` is sorted using `WebsiteData.BY_TIMESTAMP`.
     */
    static int lowerBoundTimestamp(VisitRecord[] visits, LocalDateTime t) {
        return lowerBoundTimestampRecursive(visits, new VisitRecord("", t), 0, visits.length);
    }

    // TODO 1a: Add specifications for this method, which should include an interpretation
    //  of each of the parameters, and documentation of all pre-conditions and post-conditions.

    /**
     * If the array is
     */
    @SuppressWarnings("SameParameterValue")
    static int lowerBoundTimestampRecursive(VisitRecord[] visits, VisitRecord v, int l, int r) {
        // TODO 1b: Complete the implementation of this method so that it agrees with your specifications.
        //  Your implementation must be recursive and include no loops. Use the `BY_TIMESTAMP.compare()`
        //  method for any comparisons of `VisitRecord`s.
        if (l - r < 2) {
            return l;
        }
        else {
            int m = (l + r) / 2;
            if (BY_TIMESTAMP.compare(v, visits[m]) < 0) {
                return lowerBoundTimestampRecursive(visits, v, l, m);
            }
            else {
                return lowerBoundTimestampRecursive(visits, v, m, r);
            }

        }
    }

    /**
     * Returns the index `i` with `0 <= i <= visits.length` such that `visits[..i)` all occurred
     * at or before timestamp `t` and `visits[i..]` all occurred strictly after timestamp `t`. No
     * modifications are made to the array `visits` as a result of this method. Requires that
     * `visits` is sorted using `WebsiteData.BY_TIMESTAMP`.
     */
    static int upperBoundTimestamp(VisitRecord[] visits, LocalDateTime t) {
        // TODO 2: Implement this method according to its specifications. You'll likely want to call
        //  a recursive helper method, similar to `lowerBoundTimestamp()`. Your implementation
        // (including any helper methods) must include no loops; they must achieve conditional
        // repetition using recursion. Include complete specifications for any helper methods.
        return upperBoundTimestampRecursive(visits, new VisitRecord("", t), 0, visits.length);
    }

    static int upperBoundTimestampRecursive(VisitRecord[] visits, VisitRecord v, int l, int r) {
        if (l - r < 2) {
            return l;
        }
        else {
            int m = (l + r) / 2;
            if (BY_TIMESTAMP.compare(v, visits[m]) > 0) {
                return lowerBoundTimestampRecursive(visits, v, m, r);
            }
            else {
                return lowerBoundTimestampRecursive(visits, v, l, m);
            }

        }
    }

        /**
         * Returns a reference to *new* array comprising the sorted (and possibly deduplicated) entries
         * of the given `visits` array. No modifications are made to the `visits` array as a result of
         * this method. The entries of the new array are sorted in the ascending order of the given
         * Comparator `cmp`.
         * <p> - If `policy == KEEP_ALL` then all entries of `visits` are present in the returned array,
         * and the order of equivalent elements is preserved.
         * <p> - If `policy == KEEP_FIRST`, then among all sets of equivalent entries of `visits` under
         * `cmp`, only the entry with the smallest index in `visits` is present in the returned array.
         * <p> - If `policy == KEEP_LAST`, then among all sets of equivalent entries of `visits` under
         * `cmp`, only the entry with the largest index in `visits` is present in the returned array.
         * <p> The length of the returned array will be chosen to exactly store its contents with no
         * trailing empty entries.
         */
    static VisitRecord[] deduplicatingSort(VisitRecord[] visits, Comparator<VisitRecord> cmp,
            DeduplicationPolicy policy) {
        // TODO 5a: Call dedupMergeSortRecursive(), passing in a copy of the `visits` array. Use its
        //  return value to obtain the return value for this method.
        throw new UnsupportedOperationException();
    }

    /**
     * Uses the merge sort algorithm to recursively sort `visits[begin..end)` in ascending order
     * according to the given Comparator `cmp`, while deduplicating the data according to the given
     * `policy`. Stores the sorted (and possibly deduplicated) data in `visits[begin..k)` and
     * returns `k`. The `work` array is shared by all calls to `merge()`. Requires that
     * `work.length > (end - begin) / 2`, and `0 <= begin <= end <= visits.length`.
     */
     static int dedupMergeSortRecursive(VisitRecord[] visits, VisitRecord[] work, int begin, int end,
            Comparator<VisitRecord> cmp, DeduplicationPolicy policy) {
        // TODO 5b: Implement recursive merge sort.
        throw new UnsupportedOperationException();
    }

    /**
     * Merges the sorted ranges `visits[leftBegin..leftEnd)` and `visits[rightBegin..rightEnd)` in
     * ascending order according to the given Comparator `cmp`,  while deduplicating the data
     * according to the given `policy`. Stores the merged (and possibly deduplicated) data in
     * `visits[leftBegin..k)` and returns `k`. No entries of `visits` outside `visits[leftBegin..k)`
     * are modified as a result of this method. Requires that `work.length >= leftEnd - leftBegin`,
     * `0 <= leftBegin < leftEnd <= rightBegin < rightEnd <= visits.length`, and
     * `visits[leftBegin..leftEnd)` and `visits[rightBegin..rightEnd)` are sorted and deduplicated
     * according to the given `cmp` and `policy`.
     */
    @SuppressWarnings("SameParameterValue")
    static int merge(VisitRecord[] visits, VisitRecord[] work, int leftBegin, int leftEnd,
            int rightBegin, int rightEnd, Comparator<VisitRecord> cmp, DeduplicationPolicy policy) {

        int i = 0;
        int j = rightBegin;
        int k = leftBegin;

        // Copy [leftBegin, leftEnd) to work array
        for (int z = 0; z < (leftEnd - leftBegin); z++) {
            work[z] = visits[leftBegin + z];
        }

        while (i < (leftEnd - leftBegin) || j < rightEnd) {
            int cmpValue = cmp.compare(work[i], visits[j]);

            // First timestamp comes first
            if (cmpValue < 0) {
                // Checks if last and current timestamps are the same
                if (k != leftBegin) {
                    if (cmp.compare(visits[k-1], work[i]) == 0) {
                        if (policy == KEEP_LAST) {
                            visits[k-1] = work[i];
                            i++;
                        }
                        else if (policy == KEEP_ALL) {
                            visits[k] = work[i];
                            i++; k++;
                        }
                    }
                    else {
                        visits[k] = work[i];
                        i++; k++;
                    }
                }

                // Timestamps are different
                else {
                    visits[k] = work[i];
                    i++; k++;
                }
            }

            // Second timestamp comes first
            else if (cmpValue > 0) {
                if (k != leftBegin) {
                    // Checks if current and last timestamps are the same
                    if (cmp.compare(visits[k-1], visits[j]) == 0) {
                        if (policy == KEEP_LAST) {
                            visits[k-1] = visits[j];
                            j++;
                        }
                        else if (policy == KEEP_ALL) {
                            visits[k] = visits[j];
                            j++; k++;
                        }
                    }

                    else {
                        visits[k] = visits[j];
                        j++; k++;
                    }
                }

                else {
                    visits[k] = visits[j];
                    j++; k++;
                }
            }

            // Timestamps are the same
            else {
                if (policy == KEEP_LAST) {
                    visits[k] = visits[j];
                    j++; k++;
                }
                else {
                    visits[k] = work[i];
                    i++; k++;
                }
            }
        }

        return k;
    }
}