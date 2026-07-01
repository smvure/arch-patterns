package me.svure.patterns.cqrs;

/** Events — immutable facts about what actually happened on the write side. */
public sealed interface InventoryEvent {
    String sku();

    record StockReceived(String sku, int quantity) implements InventoryEvent {}
    record StockReserved(String sku, int quantity, String orderId) implements InventoryEvent {}
}
