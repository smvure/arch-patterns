package me.svure.patterns.stranglerfig;

/** The new capability — knows a loyalty discount the legacy engine never applied. */
public final class CommerceToolsPricing implements PricingService {
    @Override
    public Price priceFor(PriceRequest r) {
        long amount = 1999L * r.quantity();
        if (r.customerSegment().equals("LOYALTY")) amount = (long) (amount * 0.9);
        return Price.usd(r.sku(), amount, "CT");
    }
}
