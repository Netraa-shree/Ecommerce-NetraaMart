package com.yournamemart.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HtmlUtilTest {

    @Test
    void escapeNullReturnsEmpty() {
        assertEquals("", HtmlUtil.escape(null));
    }

    @Test
    void escapeEscapesSpecialCharacters() {
        String input = "<script>alert(\"xss\")</script>";
        String escaped = HtmlUtil.escape(input);
        assertFalse(escaped.contains("<"));
        assertFalse(escaped.contains(">"));
        assertTrue(escaped.contains("&lt;"));
        assertTrue(escaped.contains("&gt;"));
    }

    @Test
    void escapeQuotes() {
        assertTrue(HtmlUtil.escape("say \"hello\"").contains("&quot;"));
    }
}
