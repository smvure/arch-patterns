package me.svure.patterns.stranglerfig;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StranglerFigTest {

    private static RolloutPolicy demoPolicy() {
        return r -> {
            if (r.sku().startsWith("CUTOVER")) return new Route.Modern();
            if (r.customerSegment().equals("LOYALTY")) return new Route.Shadow();
            return new Route.Legacy();
        };
    }

    @Test
    void routesLegacyCutoverAndShadow() {
        Price legacy, modern, shadow;
        int divergences;

        try (var facade = new PricingStranglerFacade(
                new LegacyAtgPricing(), new CommerceToolsPricing(), demoPolicy())) {

            legacy = facade.priceFor(new PriceRequest("SKU-1", "GUEST", 2));
            modern = facade.priceFor(new PriceRequest("CUTOVER-9", "GUEST", 1));
            shadow = facade.priceFor(new PriceRequest("SKU-1", "LOYALTY", 2));
            // close() (end of try) waits for the shadow comparison before we read the count
            facade.close();
            divergences = facade.divergencesObserved();
        }

        assertEquals("ATG", legacy.source());
        assertEquals(3998L, legacy.amountMinor());

        assertEquals("CT", modern.source());
        assertEquals(1999L, modern.amountMinor());

        // Shadow mode still serves the legacy price...
        assertEquals("ATG", shadow.source());
        assertEquals(3998L, shadow.amountMinor());

        // ...while detecting the modern engine's 10% loyalty divergence.
        assertEquals(1, divergences);
    }
}
