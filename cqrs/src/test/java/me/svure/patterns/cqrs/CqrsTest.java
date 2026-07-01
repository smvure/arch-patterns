package me.svure.patterns.cqrs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CqrsTest {

    @Test
    void projectsAvailabilityAndEnforcesTheInvariant() {
        var projector = new ReadModelProjector();
        var handler = new InventoryCommandHandler(projector);

        handler.handle(new InventoryCommand.ReceiveStock("SKU-1", 100));
        assertEquals(new StockView("SKU-1", 100, true), projector.query("SKU-1"));

        handler.handle(new InventoryCommand.ReserveStock("SKU-1", 30, "order-1"));
        assertEquals(new StockView("SKU-1", 70, true), projector.query("SKU-1"));

        // The write model refuses to over-commit.
        assertThrows(IllegalStateException.class,
                () -> handler.handle(new InventoryCommand.ReserveStock("SKU-1", 80, "order-2")));

        // ...and the read model is unchanged by the rejected command.
        assertEquals(new StockView("SKU-1", 70, true), projector.query("SKU-1"));
    }

    @Test
    void unknownSkuReadsAsOutOfStock() {
        var projector = new ReadModelProjector();
        assertEquals(new StockView("NOPE", 0, false), projector.query("NOPE"));
    }

    @Test
    void rejectsNonPositiveQuantities() {
        var handler = new InventoryCommandHandler(new ReadModelProjector());
        assertThrows(IllegalArgumentException.class,
                () -> handler.handle(new InventoryCommand.ReceiveStock("SKU-1", 0)));
    }
}
