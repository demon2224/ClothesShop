package utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class for formatting prices in Vietnamese style
 * @author Group - 07
 */
public class PriceFormatter {
    
    /**
     * Format price with Vietnamese locale (comma separator)
     * @param price the price to format
     * @return formatted price string
     */
    public static String format(double price) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(price);
    }
    
    /**
     * Format price with currency symbol
     * @param price the price to format
     * @return formatted price string with đ
     */
    public static String formatWithCurrency(double price) {
        return format(price) + "đ";
    }
}
