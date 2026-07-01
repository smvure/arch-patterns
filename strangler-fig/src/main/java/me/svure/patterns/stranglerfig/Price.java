package me.svure.patterns.stranglerfig;

public record Price(String sku, long amountMinor, String currency, String source) {
    public static Price usd(String sku, long amountMinor, String source) {
        return new Price(sku, amountMinor, "USD", source);
    }
}
