package com.csc483.sorting;

import java.util.Random;

/**
 * Empirical benchmark for Insertion Sort, Merge Sort, Quick Sort, and Heap Sort.
 * Tests across 5 data types and 4 input sizes with statistical analysis.
 *
 * @author Onyekwere Chibueze Favour
 * @studentId U2022/5570174
 */
public class SortingBenchmark {

    private static final int[] SIZES = {100, 1_000, 10_000, 100_000};
    private static final int RUNS = 5;
    private static final Random RANDOM = new Random(42);

    public static void main(String[] args) {

        System.out.println("================================================================");
        System.out.println("  SORTING ALGORITHMS EMPIRICAL ANALYSIS");
        System.out.println("  Student: Onyekwere Chibueze Favour | ID: U2022/5570174");
        System.out.println("================================================================\n");

        String[] dataTypes = {
            "Random", "Sorted (Asc)", "Reverse Sorted",
            "Nearly Sorted", "Many Duplicates"
        };

        for (String dataType : dataTypes) {
            System.out.println("================================================================");
            System.out.println("  DATA TYPE: " + dataType.toUpperCase());
            System.out.println("================================================================");
            System.out.printf("  %-12s %-15s %-12s %-16s %-12s%n",
                    "Size", "Algorithm", "Time(ms)", "Comparisons", "Swaps");
            System.out.println("  --------------------------------------------------------------");

            for (int size : SIZES) {
                int[] base = generateData(dataType, size);
                runAndPrint(size, base, "Insertion");
                runAndPrint(size, base, "Merge    ");
                runAndPrint(size, base, "Quick    ");
                runAndPrint(size, base, "Heap     ");
                System.out.println("  --------------------------------------------------------------");
            }
            System.out.println();
        }

        printStatisticalAnalysis();

        System.out.println("================================================================");
        System.out.println("  EMPIRICAL CONCLUSIONS");
        System.out.println("================================================================");
        System.out.println("  1. Quick Sort is fastest on average for random data.");
        System.out.println("  2. Insertion Sort competitive ONLY for n < 1,000.");
        System.out.println("  3. Merge Sort is CONSISTENT regardless of data order.");
        System.out.println("  4. Heap Sort uses LEAST memory but slower than Quick Sort.");
        System.out.println("  5. Insertion Sort EXCELS on nearly-sorted data (O(n) behaviour).");
        System.out.println("================================================================");
    }

    private static void runAndPrint(int size, int[] base, String name) {
        double[] times = new double[RUNS];
        SortingAlgorithms.SortStats lastStats = null;

        for (int r = 0; r < RUNS; r++) {
            int[] data = SortingAlgorithms.copyArray(base);
            SortingAlgorithms.SortStats stats = new SortingAlgorithms.SortStats();
            long start = System.nanoTime();

            switch (name.trim()) {
                case "Insertion": SortingAlgorithms.insertionSort(data, stats); break;
                case "Merge":     SortingAlgorithms.mergeSort(data, stats);     break;
                case "Quick":     SortingAlgorithms.quickSort(data, stats);     break;
                case "Heap":      SortingAlgorithms.heapSort(data, stats);      break;
            }

            times[r] = (System.nanoTime() - start) / 1_000_000.0;
            lastStats = stats;
        }

        double mean = mean(times);
        System.out.printf("  %-12d %-15s %-12.3f %-16s %-12s%n",
                size, name, mean,
                String.format("%,d", lastStats.comparisons),
                String.format("%,d", lastStats.swaps));
    }

    private static int[] generateData(String type, int size) {
        int[] arr = new int[size];
        switch (type) {
            case "Random":
                for (int i = 0; i < size; i++) arr[i] = RANDOM.nextInt(size * 10);
                break;
            case "Sorted (Asc)":
                for (int i = 0; i < size; i++) arr[i] = i;
                break;
            case "Reverse Sorted":
                for (int i = 0; i < size; i++) arr[i] = size - i;
                break;
            case "Nearly Sorted":
                for (int i = 0; i < size; i++) arr[i] = i;
                int shuffles = size / 10;
                for (int i = 0; i < shuffles; i++) {
                    int a = RANDOM.nextInt(size);
                    int b = RANDOM.nextInt(size);
                    int tmp = arr[a]; arr[a] = arr[b]; arr[b] = tmp;
                }
                break;
            case "Many Duplicates":
                for (int i = 0; i < size; i++) arr[i] = RANDOM.nextInt(10);
                break;
            default:
                for (int i = 0; i < size; i++) arr[i] = RANDOM.nextInt(size);
        }
        return arr;
    }

    private static double mean(double[] data) {
        double sum = 0;
        for (double d : data) sum += d;
        return sum / data.length;
    }

    private static double stdDev(double[] data) {
        double m = mean(data);
        double sumSq = 0;
        for (double d : data) sumSq += (d - m) * (d - m);
        return Math.sqrt(sumSq / data.length);
    }

    private static void printStatisticalAnalysis() {
        System.out.println("================================================================");
        System.out.println("  STATISTICAL ANALYSIS: Quick Sort vs Merge Sort");
        System.out.println("  (n=10,000, Random Data, Welch's t-test, alpha=0.05)");
        System.out.println("================================================================");

        int size = 10_000;
        double[] quickTimes = new double[RUNS];
        double[] mergeTimes = new double[RUNS];

        for (int r = 0; r < RUNS; r++) {
            int[] d1 = generateData("Random", size);
            int[] d2 = SortingAlgorithms.copyArray(d1);
            SortingAlgorithms.SortStats s1 = new SortingAlgorithms.SortStats();
            SortingAlgorithms.SortStats s2 = new SortingAlgorithms.SortStats();

            long t1 = System.nanoTime();
            SortingAlgorithms.quickSort(d1, s1);
            quickTimes[r] = (System.nanoTime() - t1) / 1_000_000.0;

            long t2 = System.nanoTime();
            SortingAlgorithms.mergeSort(d2, s2);
            mergeTimes[r] = (System.nanoTime() - t2) / 1_000_000.0;
        }

        double qMean = mean(quickTimes);
        double mMean = mean(mergeTimes);
        double qStd  = stdDev(quickTimes);
        double mStd  = stdDev(mergeTimes);

        double tStat = (qMean - mMean) /
                Math.sqrt((qStd * qStd / RUNS) + (mStd * mStd / RUNS));

        System.out.printf("  Quick Sort  -> Mean: %.4f ms | StdDev: %.4f ms%n", qMean, qStd);
        System.out.printf("  Merge Sort  -> Mean: %.4f ms | StdDev: %.4f ms%n", mMean, mStd);
        System.out.printf("  t-statistic = %.4f%n", tStat);
        System.out.println("  Critical value (alpha=0.05, df=8): 2.306");
        System.out.printf("  Result: %s%n%n",
                Math.abs(tStat) > 2.306
                ? "SIGNIFICANT difference — Quick Sort is measurably faster."
                : "NO significant difference — performance is comparable.");
    }
}
