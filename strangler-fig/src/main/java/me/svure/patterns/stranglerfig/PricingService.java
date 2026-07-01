package me.svure.patterns.stranglerfig;

/** The capability being strangled. */
public interface PricingService {
    Price priceFor(PriceRequest request);
}
