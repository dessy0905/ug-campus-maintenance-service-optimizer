package gh.edu.ug.cs.ugmaintenance.algorithms.searching;

import java.util.Objects;

public final class LinearSearch {
    private LinearSearch() {
    }

    public static int search(int[] values, int target) {
        if (values == null) {
            return -1;
        }

        for (int index = 0; index < values.length; index++) {
            if (values[index] == target) {
                return index;
            }
        }

        return -1;
    }

    public static <T> int search(T[] values, T target) {
        if (values == null) {
            return -1;
        }

        for (int index = 0; index < values.length; index++) {
            if (Objects.equals(values[index], target)) {
                return index;
            }
        }

        return -1;
    }

    public static boolean contains(int[] values, int target) {
        return search(values, target) != -1;
    }

    public static <T> boolean contains(T[] values, T target) {
        return search(values, target) != -1;
    }
}
