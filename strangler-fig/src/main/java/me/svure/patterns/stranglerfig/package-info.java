/**
 * Strangler Fig — retire a monolith by routing traffic through a facade, capability by capability.
 *
 * <p>Models the migration of pricing: a {@link me.svure.patterns.stranglerfig.PricingStranglerFacade
 * facade} routes each request to the legacy or modern engine, or runs in <em>shadow</em> mode —
 * serving legacy while comparing the modern answer on a virtual thread.</p>
 *
 * <p>Companion to the pattern page at https://svure.me/patterns/strangler-fig/</p>
 */
package me.svure.patterns.stranglerfig;
