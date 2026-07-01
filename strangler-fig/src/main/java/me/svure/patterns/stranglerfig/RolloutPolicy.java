package me.svure.patterns.stranglerfig;

/** The runtime decision of which path serves a request — a value you change, not a deploy. */
@FunctionalInterface
public interface RolloutPolicy {
    Route decide(PriceRequest request);
}
