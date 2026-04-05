package com.csc483.search;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements sequential search, binary search, and hybrid name-index search.
 *
 * @author Onyekwere Chibueze Favour
 * @studentId U2022/5570174
 */
public class SearchAlgorithms {

    /**
     * Sequential search by product ID.
     * Best: O(1) | Average: O(n) | Worst: O(n)
     */
    public static Product sequentialSearchById(Product[] products, int targetId) {
        if (products == null) return null;
        for (int i = 0; i < products.length; i++) {
            if (products[i] != null && products[i].getProductId() == targetId) {
                return products[i];
            }
        }
        return null;
    }

    /**
     * Binary search by product ID.
     * PRECONDITION: Array must be sorted by productId ascending.
     * Best: O(1) | Average: O(log n) | Worst: O(log n)
     *
     * For n=100,000:
     * Sequential worst = 100,000 comparisons
     * Binary worst     = ceil(log2(100,000)) = 17 comparisons
     * Speedup          = 100,000 / 17 = ~5,882x faster
     */
    public static Product binarySearchById(Product[] products, int targetId) {
        if (products == null) return null;
        int low = 0;
        int high = products.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (products[mid] == null) { high = mid - 1; continue; }

            int midId = products[mid].getProductId();
            if (midId == targetId)      return products[mid];
            else if (midId < targetId)  low = mid + 1;
            else                        high = mid - 1;
        }
        return null;
    }

    /**
     * Sequential search by product name (case-insensitive).
     * Binary search cannot apply here — array is sorted by ID, not name.
     * Time Complexity: O(n) always
     */
    public static Product searchByName(Product[] products, String targetName) {
        if (products == null || targetName == null) return null;
        for (Product product : products) {
            if (product != null && product.getProductName().equalsIgnoreCase(targetName)) {
                return product;
            }
        }
        return null;
    }

    /**
     * Builds a HashMap index for O(1) average name lookups.
     * Build time: O(n) | Space: O(n)
     */
    public static Map<String, Product> buildNameIndex(Product[] products) {
        Map<String, Product> nameIndex = new HashMap<>();
        if (products == null) return nameIndex;
        for (Product product : products) {
            if (product != null) {
                nameIndex.put(product.getProductName().toLowerCase(), product);
            }
        }
        return nameIndex;
    }

    /**
     * O(1) average name lookup using pre-built HashMap index.
     */
    public static Product hybridNameSearch(Map<String, Product> nameIndex, String targetName) {
        if (nameIndex == null || targetName == null) return null;
        return nameIndex.get(targetName.toLowerCase());
    }

    /**
     * Inserts a product into a sorted array maintaining sorted order.
     * Time Complexity: O(n) worst case (shifting elements)
     * Space Complexity: O(1) in-place
     */
    public static boolean addProduct(Product[] products, Product newProduct) {
        if (products == null || newProduct == null) return false;

        int size = 0;
        for (Product p : products) { if (p != null) size++; }
        if (size >= products.length) return false;

        int insertPos = size;
        for (int i = 0; i < size; i++) {
            if (products[i].getProductId() > newProduct.getProductId()) {
                insertPos = i;
                break;
            }
        }

        for (int i = size; i > insertPos; i--) {
            products[i] = products[i - 1];
        }

        products[insertPos] = newProduct;
        return true;
    }
}
