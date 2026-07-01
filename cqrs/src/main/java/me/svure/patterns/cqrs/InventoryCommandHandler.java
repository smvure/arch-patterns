package me.svure.patterns.cqrs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WRITE side front door. Applies the command to the write model (which may reject it) and,
 * only on success, emits the corresponding event and forwards it to the read side.
 */
public final class InventoryCommandHandler {

    private final Map<String, StockItem> writeStore = new ConcurrentHashMap<>();
    private final ReadModelProjector projector;

    public InventoryCommandHandler(ReadModelProjector projector) {
        this.projector = projector;
    }

    public InventoryEvent handle(InventoryCommand command) {
        var item = writeStore.computeIfAbsent(command.sku(), StockItem::new);
        InventoryEvent event = switch (command) {
            case InventoryCommand.ReceiveStock(var sku, var qty) -> {
                item.receive(qty);                                   // invariant checked here
                yield new InventoryEvent.StockReceived(sku, qty);
            }
            case InventoryCommand.ReserveStock(var sku, var qty, var orderId) -> {
                item.reserve(qty);                                   // throws if insufficient
                yield new InventoryEvent.StockReserved(sku, qty, orderId);
            }
        };
        projector.apply(event);                                      // forward the fact to the read side
        return event;
    }
}
