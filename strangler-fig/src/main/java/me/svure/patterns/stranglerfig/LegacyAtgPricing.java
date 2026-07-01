package me.svure.patterns.stranglerfig;

/** The monolith path — flat pricing, no segment logic. */
public final class LegacyAtgPricing implements PricingService {
    @Override
    public Price priceFor(PriceRequest r) {
        return Price.usd(r.sku(), 1999L * r.quantity(), "ATG");
    }
}
