package com.yournamemart.util;

/**
 * Simple HTML escaping for user-supplied text rendered in JSP.
 * Prefer JSTL c:out when possible; this is for servlet-side use.
 */
public final class HtmlUtil {

    private HtmlUtil() {}

    public static String escape(String input) {
        if (input == null) return "";
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
