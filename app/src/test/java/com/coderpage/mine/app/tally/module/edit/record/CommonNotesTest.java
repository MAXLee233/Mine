package com.coderpage.mine.app.tally.module.edit.record;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CommonNotesTest {
    @Test public void trimsBlankLinesAndDeduplicatesWithoutChangingOrder() {
        assertEquals(Arrays.asList("午餐", "早餐", "外卖"),
                CommonNotes.parse(" 午餐 \r\n\r\n早餐\n午餐\n 外卖 "));
    }

    @Test public void clearingAllPresetsProducesAnEmptyList() {
        assertEquals(Collections.emptyList(), CommonNotes.parse(" \r\n\n"));
        assertEquals(Collections.emptyList(), CommonNotes.parse(null));
    }

    @Test public void preservesPunctuationAndSpacesInsideNotes() {
        assertEquals(Arrays.asList("午餐 和同事", "咖啡/奶茶：20元"),
                CommonNotes.parse("午餐 和同事\n咖啡/奶茶：20元"));
    }

    @Test public void savedPresetsCanBeReadBackInTheSameOrder() {
        assertEquals(Arrays.asList("早餐", "晚餐"), CommonNotes.parse(
                CommonNotes.format(Arrays.asList("早餐", "晚餐"))));
        assertEquals("", CommonNotes.format(Collections.emptyList()));
    }

    @Test public void normalizesPastedWindowsAndMacLineEndings() {
        assertEquals(Arrays.asList("早餐", "午餐", "晚餐"),
                CommonNotes.parse("早餐\r午餐\r\n晚餐"));
    }

    @Test public void separatesCategoriesAccountsAndRecordTypes() {
        assertNotEquals(CommonNotes.key(0, 0, "CanYin"), CommonNotes.key(0, 0, "JiaoTong"));
        assertNotEquals(CommonNotes.key(0, 0, "Other"), CommonNotes.key(0, 1, "Other"));
        assertNotEquals(CommonNotes.key(0, 0, "CanYin"), CommonNotes.key(1, 0, "CanYin"));
    }
}
