package com.csc483.search;

/**
 * Represents a product in the TechMart online store inventory.
 *
 * @author Onyekwere Chibueze Favour
 * @studentId U2022/5570174
 * @course CSC 483.1 - Algorithms Analysis and Design
 */
public class Product implements Comparable<Product> {

    private int productId;
    private String productName;
    private String category;
    private double price;
    private int stockQuantity;

    public Product(int productId, String productName, String category,
                   double price, int stockQuantity) {
        if (productId <= 0) throw new IllegalArgumentException("Product ID must be positive.");
        if (productName == null || productName.trim().isEmpty())
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        if (stockQuantity < 0) throw new IllegalArgumentException("Stock cannot be negative.");

        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        this.price = price;
    }

    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) throw new IllegalArgumentException("Stock cannot be negative.");
        this.stockQuantity = stockQuantity;
    }

    @Override
    public int compareTo(Product other) {
        return Integer.compare(this.productId, other.productId);
    }

    @Override
    public String toString() {
        return String.format("Product{ID=%d, Name='%s', Category='%s', Price=%.2f, Stock=%d}",
                productId, productName, category, price, stockQuantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product)) return false;
        Product other = (Product) obj;
        return this.productId == other.productId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(productId);
    }
            }
