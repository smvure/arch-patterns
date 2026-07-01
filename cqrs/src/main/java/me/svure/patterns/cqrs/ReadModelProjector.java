package me.svure.patterns.cqrs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * READ side — folds events into a precomputed answer (available-per-SKU) and serves the
 * fast query. Holds no business rules; it is derived entirely from events and disposable.
 */
public final class ReadModelProjector {

    private final Map<String, Integer> available = new ConcurrentHashMap<>();

    /** Exhaustive over the sealed event type; record patterns destructure, {@code _} drops the unused field. */
    public void apply(InventoryEvent event) {
        switch (event) {
            case InventoryEvent.StockReceived(var sku, var qty)    -> available.merge(sku, qty, Integer::sum);
            case InventoryEvent.StockReserved(var sku, var qty, _) -> available.merge(sku, -qty, Integer::sum);
        }
    }

    /** The fast path — a single map lookup, no joins. */
    public StockView query(String sku) {
        int qty = available.getOrDefault(sku, 0);
        return new StockView(sku, qty, qty > 0);
    }
}
