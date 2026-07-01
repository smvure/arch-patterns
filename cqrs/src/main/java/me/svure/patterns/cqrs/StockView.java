package me.svure.patterns.cqrs;

/** READ model shape — a denormalized projection answered in a single lookup. */
public record StockView(String sku, int available, boolean inStock) {}
