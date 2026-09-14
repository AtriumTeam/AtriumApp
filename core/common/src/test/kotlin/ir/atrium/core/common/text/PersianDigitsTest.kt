package ir.atrium.core.common.text

import org.junit.Assert.assertEquals
import org.junit.Test

class PersianDigitsTest {

    @Test
    fun `converts ascii digits`() {
        assertEquals("۱۴۰۵", 1405.toPersianDigits())
    }

    @Test
    fun `leaves non digits untouched`() {
        assertEquals("نسخهٔ ۱.۰.۰", "نسخهٔ 1.0.0".toPersianDigits())
    }

    @Test
    fun `round trips back to latin`() {
        assertEquals("2026", "2026".toPersianDigits().toLatinDigits())
    }
}
