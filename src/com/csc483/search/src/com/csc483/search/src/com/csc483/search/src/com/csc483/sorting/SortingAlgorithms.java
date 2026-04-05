package com.csc483.sorting;

/**
 * Implements Insertion Sort, Merge Sort, Quick Sort, and Heap Sort from scratch.
 * Each method tracks comparison and swap counts for empirical analysis.
 *
 * @author Onyekwere Chibueze Favour
 * @studentId U2022/5570174
 */
public class SortingAlgorithms {

    public static class SortStats {
        public long comparisons = 0;
        public long swaps = 0;

        @Override
        public String toString() {
            return String.format("Comparisons=%,d | Swaps=%,d", comparisons, swaps);
        }
    }

    /**
     * Insertion Sort
     * Best: O(n) | Average: O(n²) | Worst: O(n²)
     * Stable: YES | In-Place: YES
     * Best for: small arrays, nearly-sorted data
     */
    public static void insertionSort(int[] arr, SortStats stats) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                stats.comparisons++;
                arr[j + 1] = arr[j];
                stats.swaps++;
                j--;
            }
            if (j >= 0) stats.comparisons++;
            arr[j + 1] = key;
        }
    }

    /**
     * Merge Sort
     * Best: O(n log n) | Average: O(n log n) | Worst: O(n log n)
     * Stable: YES | In-Place: NO — requires O(n) extra space
     * Best for: guaranteed performance, stability required
     */
    public static void mergeSort(int[] arr, SortStats stats) {
        if (arr == null || arr.length <= 1) return;
        mergeSortHelper(arr, 0, arr.length - 1, stats);
    }

    private static void mergeSortHelper(int[] arr, int left, int right, SortStats stats) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortHelper(arr, left, mid, stats);
            mergeSortHelper(arr, mid + 1, right, stats);
            merge(arr, left, mid, right, stats);
        }
    }

    private static void merge(int[] arr, int left, int mid, int right, SortStats stats) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] L = new int[n1];
        int[] R = new int[n2];
        System.arraycopy(arr, left, L, 0, n1);
        System.arraycopy(arr, mid + 1, R, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            stats.comparisons++;
            if (L[i] <= R[j]) arr[k++] = L[i++];
            else               arr[k++] = R[j++];
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    /**
     * Quick Sort with median-of-three pivot selection
     * Best: O(n log n) | Average: O(n log n) | Worst: O(n²)
     * Stable: NO | In-Place: YES
     * Best for: random data, cache-efficient sorting
     */
    public static void quickSort(int[] arr, SortStats stats) {
        if (arr == null || arr.length <= 1) return;
        quickSortHelper(arr, 0, arr.length - 1, stats);
    }

    private static void quickSortHelper(int[] arr, int low, int high, SortStats stats) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high, stats);
            quickSortHelper(arr, low, pivotIndex - 1, stats);
            quickSortHelper(arr, pivotIndex + 1, high, stats);
        }
    }

    private static int partition(int[] arr, int low, int high, SortStats stats) {
        int mid = low + (high - low) / 2;
        if (arr[low] > arr[mid])  swap(arr, low, mid, stats);
        if (arr[low] > arr[high]) swap(arr, low, high, stats);
        if (arr[mid] > arr[high]) swap(arr, mid, high, stats);
        swap(arr, mid, high, stats);
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            stats.comparisons++;
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j, stats);
            }
        }
        swap(arr, i + 1, high, stats);
        return i + 1;
    }

    /**
     * Heap Sort
     * Best: O(n log n) | Average: O(n log n) | Worst: O(n log n)
     * Stable: NO | In-Place: YES
     * Best for: memory-constrained environments
     */
    public static void heapSort(int[] arr, SortStats stats) {
        if (arr == null || arr.length <= 1) return;
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i, stats);
        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i, stats);
            heapify(arr, i, 0, stats);
        }
    }

    private static void heapify(int[] arr, int n, int root, SortStats stats) {
        int largest = root;
        int left  = 2 * root + 1;
        int right = 2 * root + 2;
        if (left  < n) { stats.comparisons++; if (arr[left]  > arr[largest]) largest = left;  }
        if (right < n) { stats.comparisons++; if (arr[right] > arr[largest]) largest = right; }
        if (largest != root) {
            swap(arr, root, largest, stats);
            heapify(arr, n, largest, stats);
        }
    }

    private static void swap(int[] arr, int i, int j, SortStats stats) {
        if (i != j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
            stats.swaps++;
        }
    }

    public static int[] copyArray(int[] arr) {
        int[] copy = new int[arr.length];
        System.arraycopy(arr, 0, copy, 0, arr.length);
        return copy;
    }
}
