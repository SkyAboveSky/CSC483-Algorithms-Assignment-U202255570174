package com.csc483.search;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

/**
 * Benchmark program for TechMart Search Performance Analysis.
 *
 * @author Onyekwere Chibueze Favour
 * @studentId U2022/5570174
 */
public class TechMartBenchmark {

    private static final int DATASET_SIZE   = 100_000;
    private static final int MAX_PRODUCT_ID = 200_000;
    private static final int BENCHMARK_RUNS = 5;

    private static final String[] CATEGORIES = {
        "Laptop", "Smartphone", "Tablet", "Monitor", "Keyboard",
        "Mouse", "Headphone", "Speaker", "Camera", "Printer"
    };

    private static final String[] NAME_PREFIXES = {
        "TechPro", "UltraMax", "SmartEdge", "NovaTech", "SwiftCore",
        "AlphaWave", "ZenTech", "PeakLine", "FusionX", "PrimeTech"
    };

    public static void main(String[] args) {

        System.out.println("================================================================");
        System.out.println("  TECHMART SEARCH PERFORMANCE ANALYSIS (n = 100,000 products)  ");
        System.out.println("  Student: Onyekwere Chibueze Favour | ID: U2022/5570174       ");
        System.out.println("================================================================\n");

        System.out.println("[*] Generating " + DATASET_SIZE + " random products...");
        Product[] products = generateProducts(DATASET_SIZE);

        System.out.println("[*] Sorting array by productId for binary search...\n");
        Arrays.sort(products);

        int bestCaseId    = products[DATASET_SIZE / 2].getProductId();
        int averageCaseId = products[DATASET_SIZE / 4 * 3].getProductId();
        int worstCaseId   = MAX_PRODUCT_ID + 999;
        int seqBestCaseId = products[0].getProductId();

        System.out.println("----------------------------------------------------------------");
        System.out.println("  SEQUENTIAL SEARCH RESULTS");
        System.out.println("----------------------------------------------------------------");
        double seqBest    = benchmarkSequential(products, seqBestCaseId);
        double seqAverage = benchmarkSequential(products, averageCaseId);
        double seqWorst   = benchmarkSequential(products, worstCaseId);
        System.out.printf("  Best Case  (ID at position 0)    : %.4f ms%n", seqBest);
        System.out.printf("  Average Case (ID at ~75%% mark)   : %.4f ms%n", seqAverage);
        System.out.printf("  Worst Case (ID not in array)     : %.4f ms%n%n", seqWorst);

        System.out.println("----------------------------------------------------------------");
        System.out.println("  BINARY SEARCH RESULTS");
        System.out.println("----------------------------------------------------------------");
        double binBest    = benchmarkBinary(products, bestCaseId);
        double binAverage = benchmarkBinary(products, averageCaseId);
        double binWorst   = benchmarkBinary(products, worstCaseId);
        System.out.printf("  Best Case  (ID at middle)        : %.4f ms%n", binBest);
        System.out.printf("  Average Case (ID at ~75%% mark)   : %.4f ms%n", binAverage);
        System.out.printf("  Worst Case (ID not in array)     : %.4f ms%n%n", binWorst);

        double speedup = seqAverage / binAverage;
        System.out.println("----------------------------------------------------------------");
        System.out.printf("  PERFORMANCE IMPROVEMENT: Binary search is ~%.0fx faster%n", speedup);
        System.out.println("  THEORETICAL: n / ceil(log2(n)) = 100,000 / 17 = ~5,882x");
        System.out.println("----------------------------------------------------------------\n");

        System.out.println("----------------------------------------------------------------");
        System.out.println("  HYBRID NAME SEARCH (HashMap Index)");
        System.out.println("----------------------------------------------------------------");
        long buildStart = System.nanoTime();
        Map<String, Product> nameIndex = SearchAlgorithms.buildNameIndex(products);
        double buildTime = (System.nanoTime() - buildStart) / 1_000_000.0;

        String targetName = products[50_000].getProductName();
        double hybridTime = benchmarkHybridSearch(nameIndex, targetName);

        Product[] extended = Arrays.copyOf(products, DATASET_SIZE + 10);
        Product newProduct = new Product(150001, "NovaTech X1 Pro", "Laptop", 450000.00, 5);
        long insStart = System.nanoTime();
        boolean inserted = SearchAlgorithms.addProduct(extended, newProduct);
        double insertTime = (System.nanoTime() - insStart) / 1_000_000.0;

        System.out.printf("  Index Build Time                 : %.4f ms%n", buildTime);
        System.out.printf("  Average Hybrid Search Time       : %.4f ms%n", hybridTime);
        System.out.printf("  addProduct (sorted insert)       : %.4f ms | Success: %b%n%n", insertTime, inserted);

        printComplexityTable();

        System.out.println("================================================================");
        System.out.println("  END OF TECHMART SEARCH PERFORMANCE ANALYSIS");
        System.out.println("================================================================");
    }

    private static double benchmarkSequential(Product[] products, int targetId) {
        double total = 0;
        for (int i = 0; i < BENCHMARK_RUNS; i++) {
            long start = System.nanoTime();
            SearchAlgorithms.sequentialSearchById(products, targetId);
            total += (System.nanoTime() - start);
        }
        return (total / BENCHMARK_RUNS) / 1_000_000.0;
    }

    private static double benchmarkBinary(Product[] products, int targetId) {
        double total = 0;
        for (int i = 0; i < BENCHMARK_RUNS; i++) {
            long start = System.nanoTime();
            SearchAlgorithms.binarySearchById(products, targetId);
            total += (System.nanoTime() - start);
        }
        return (total / BENCHMARK_RUNS) / 1_000_000.0;
    }

    private static double benchmarkHybridSearch(Map<String, Product> index, String name) {
        double total = 0;
        for (int i = 0; i < BENCHMARK_RUNS; i++) {
            long start = System.nanoTime();
            SearchAlgorithms.hybridNameSearch(index, name);
            total += (System.nanoTime() - start);
        }
        return (total / BENCHMARK_RUNS) / 1_000_000.0;
    }

    private static Product[] generateProducts(int n) {
        Product[] products = new Product[n];
        boolean[] usedIds = new boolean[MAX_PRODUCT_ID + 1];
        Random random = new Random(42);
        int count = 0;
        while (count < n) {
            int id = random.nextInt(MAX_PRODUCT_ID) + 1;
            if (!usedIds[id]) {
                usedIds[id] = true;
                String name = NAME_PREFIXES[random.nextInt(NAME_PREFIXES.length)]
                        + " " + (char)('A' + random.nextInt(26)) + random.nextInt(9999);
                String category = CATEGORIES[random.nextInt(CATEGORIES.length)];
                double price = 5000 + (random.nextDouble() * 995000);
                int stock = random.nextInt(500);
                products[count++] = new Product(id, name, category, price, stock);
            }
        }
        return products;
    }

    private static void printComplexityTable() {
        System.out.println("  ALGORITHM COMPLEXITY SUMMARY");
        System.out.println("  +--------------------------+----------+----------+----------+---------+");
        System.out.println("  | Algorithm                | Best     | Average  | Worst    | Space   |");
        System.out.println("  +--------------------------+----------+----------+----------+---------+");
        System.out.println("  | Sequential Search        | O(1)     | O(n)     | O(n)     | O(1)    |");
        System.out.println("  | Binary Search            | O(1)     | O(log n) | O(log n) | O(1)    |");
        System.out.println("  | Hybrid Name Search       | O(1)     | O(1)     | O(n)*    | O(n)    |");
        System.out.println("  | addProduct (sorted ins)  | O(1)     | O(n)     | O(n)     | O(1)    |");
        System.out.println("  +--------------------------+----------+----------+----------+---------+");
        System.out.println("  * HashMap worst case O(n) occurs only on hash collision chains\n");
    }
}
