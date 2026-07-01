package me.svure.patterns.stranglerfig;

/** A closed set of routing outcomes — the compiler enforces exhaustive handling. */
public sealed interface Route permits Route.Legacy, Route.Modern, Route.Shadow {
    record Legacy() implements Route {}
    record Modern() implements Route {}
    record Shadow() implements Route {}   // serve legacy, observe modern in the background
}
