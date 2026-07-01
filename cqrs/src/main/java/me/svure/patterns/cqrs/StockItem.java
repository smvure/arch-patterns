package me.svure.patterns.cqrs;

/**
 * WRITE model — strict and normalized. Its only job is to keep {@code available} honest
 * and refuse anything that would break the invariant. It knows nothing about how data is read.
 */
final class StockItem {
    private final String sku;
    private int onHand;     // physically in the warehouse
    private int reserved;   // committed to orders

    StockItem(String sku) { this.sku = sku; }

    int available() { return onHand - reserved; }

    void receive(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("quantity must be positive");
        onHand += qty;
    }

    void reserve(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("quantity must be positive");
        if (qty > available())
            throw new IllegalStateException("insufficient stock: want %d, have %d".formatted(qty, available()));
        reserved += qty;
    }
}
