package me.svure.patterns.cqrs;

/** Commands — intents to change state. The write side accepts or rejects each one. */
public sealed interface InventoryCommand {
    String sku();

    record ReceiveStock(String sku, int quantity) implements InventoryCommand {}
    record ReserveStock(String sku, int quantity, String orderId) implements InventoryCommand {}
}
