package me.svure.patterns.stranglerfig;

/** Runnable walkthrough: a legacy route, a cut-over route, and a shadowed route that logs a divergence. */
public final class StranglerFigDemo {

    public static void main(String[] args) {
        PricingService legacy = new LegacyAtgPricing();
        PricingService modern = new CommerceToolsPricing();

        // The rollout policy IS the migration — change it at runtime, no redeploy.
        RolloutPolicy policy = r -> {
            if (r.sku().startsWith("CUTOVER")) return new Route.Modern();          // fully migrated SKUs
            if (r.customerSegment().equals("LOYALTY")) return new Route.Shadow();   // observe, don't serve yet
            return new Route.Legacy();                                             // everything else
        };

        try (var facade = new PricingStranglerFacade(legacy, modern, policy)) {
            System.out.println(facade.priceFor(new PriceRequest("SKU-1", "GUEST", 2)));      // ATG 3998
            System.out.println(facade.priceFor(new PriceRequest("CUTOVER-9", "GUEST", 1)));  // CT  1999
            System.out.println(facade.priceFor(new PriceRequest("SKU-1", "LOYALTY", 2)));    // ATG 3998 (+divergence)
        }   // close() waits for the shadow comparison to finish and print
    }
}
