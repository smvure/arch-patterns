package me.svure.patterns.stranglerfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The stable front door. Routes each request per the rollout policy. In shadow mode it serves
 * the legacy price and compares the modern engine on a virtual thread, logging divergences.
 *
 * <p>{@link AutoCloseable}: closing awaits in-flight shadow comparisons (so divergence counts
 * are stable to read), then shuts the pool down.</p>
 */
public final class PricingStranglerFacade implements PricingService, AutoCloseable {

    private final PricingService legacy;
    private final PricingService modern;
    private final RolloutPolicy policy;
    private final ExecutorService shadowPool = Executors.newVirtualThreadPerTaskExecutor();
    private final AtomicInteger divergences = new AtomicInteger();

    public PricingStranglerFacade(PricingService legacy, PricingService modern, RolloutPolicy policy) {
        this.legacy = legacy;
        this.modern = modern;
        this.policy = policy;
    }

    /** Divergences observed by shadow comparisons. Read after {@link #close()} for a stable value. */
    public int divergencesObserved() {
        return divergences.get();
    }

    @Override
    public Price priceFor(PriceRequest r) {
        return switch (policy.decide(r)) {
            case Route.Modern _ -> modern.priceFor(r);              // cut over
            case Route.Legacy _ -> legacy.priceFor(r);              // untouched
            case Route.Shadow _ -> shadowCompareThenServeLegacy(r);
        };
    }

    private Price shadowCompareThenServeLegacy(PriceRequest r) {
        Price legacyPrice = legacy.priceFor(r);                     // what the customer gets
        shadowPool.submit(() -> {                                   // fire-and-observe, never blocks serving
            Price modernPrice = modern.priceFor(r);
            if (modernPrice.amountMinor() != legacyPrice.amountMinor()) {
                divergences.incrementAndGet();
                System.out.printf("DIVERGENCE sku=%s seg=%s legacy=%d modern=%d%n",
                        r.sku(), r.customerSegment(), legacyPrice.amountMinor(), modernPrice.amountMinor());
            }
        });
        return legacyPrice;
    }

    @Override
    public void close() {
        shadowPool.close();   // ExecutorService is AutoCloseable since Java 19 — awaits termination
    }
}
