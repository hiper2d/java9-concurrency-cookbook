import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AppTest {

    @Test
    fun test() {
        val a: Short = Double.POSITIVE_INFINITY.toShort()
        assertEquals(-1,  a)
    }
}