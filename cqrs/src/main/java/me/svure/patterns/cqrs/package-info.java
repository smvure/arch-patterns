/**
 * CQRS — Command Query Responsibility Segregation.
 *
 * <p>An inventory example: a strict {@link me.svure.patterns.cqrs.StockItem write model}
 * enforces the invariant, while a denormalized {@link me.svure.patterns.cqrs.ReadModelProjector
 * read model} serves the "in stock?" query. Commands and events are sealed records.</p>
 *
 * <p>Companion to the pattern page at https://svure.me/patterns/cqrs/</p>
 */
package me.svure.patterns.cqrs;
