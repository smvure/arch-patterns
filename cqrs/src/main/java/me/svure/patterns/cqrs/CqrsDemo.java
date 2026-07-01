package me.svure.patterns.cqrs;

/** Runnable walkthrough: receive 100, reserve 30, then a rejected reservation of 80. */
public final class CqrsDemo {

    public static void main(String[] args) {
        var projector = new ReadModelProjector();
        var handler = new InventoryCommandHandler(projector);

        handler.handle(new InventoryCommand.ReceiveStock("SKU-1", 100));
        handler.handle(new InventoryCommand.ReserveStock("SKU-1", 30, "order-1"));
        System.out.println(projector.query("SKU-1"));   // available=70, inStock=true

        try {
            handler.handle(new InventoryCommand.ReserveStock("SKU-1", 80, "order-2"));
        } catch (IllegalStateException e) {
            System.out.println("rejected: " + e.getMessage());   // 80 > 70 available
        }

        System.out.println(projector.query("SKU-1"));   // still 70 — read model unchanged
    }
}
