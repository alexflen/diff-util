import kotlin.test.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class LineNumbers(val lineIn1: Int, val lineIn2: Int)
class LineInfo(val added: List<LineNumbers>, val deleted: List<LineNumbers>, val unchanged: List<LineNumbers>)

internal class Test1 {
    private val standardOut = System.out
    private val standardIn = System.`in`
    private val stream = ByteArrayOutputStream()

    @BeforeTest
    fun setUp() {
        System.setOut(PrintStream(stream))
    }

    @AfterTest
    fun tearDown() {
        System.setOut(standardOut)
        System.setIn(standardIn)
    }

    /*@Test
    fun test1() {
        computeFib(arrayOf("1"))
        assertEquals("1", stream.toString().trim())
    }

    @Test
    fun test2() {
        System.setIn(ByteArrayInputStream("10".toByteArray()))
        computeFib(arrayOf())
        assertEquals("55", stream.toString().trim().lines().last())
    }*/
    @Test
    fun parseInputTest1() {
        System.setIn(ByteArrayInputStream(("str1   str2 str3  ").toByteArray()))
        assertEquals(listOf("str1", "str2", "str3"), parseInput())
    }

    @Test
    fun parseInputTest2() {
        System.setIn(ByteArrayInputStream(("file1.txt file2.txt > answer.txt").toByteArray()))
        assertEquals(listOf("file1.txt", "file2.txt", ">", "answer.txt"), parseInput())
    }

    @Test
    fun parseInputTest3() {
        System.setIn(ByteArrayInputStream(("").toByteArray()))
        parseInput()
        assertEquals("Недостаточно параметров для вызова функции", stream.toString().trim().lines().last())
    }

    @Test
    fun findLCSTest1() {
        val ret = findLCS(listOf("abacaba", "baca"), listOf("abacaba", "baca"))
        assertEquals(0, ret.added.size)
        assertEquals(0, ret.deleted.size)
        assertEquals(2, ret.unchanged.size)

        assertEquals(0, ret.unchanged[0].lineIn1)
        assertEquals(0, ret.unchanged[0].lineIn2)
        assertEquals(1, ret.unchanged[1].lineIn1)
        assertEquals(1, ret.unchanged[1].lineIn2)
    }

    @Test
    fun findLCSTest2() {
        val ret = findLCS(listOf("abacaba", "baca"), listOf("baca", "baca"))
        assertEquals(1, ret.added.size)
        assertEquals(1, ret.deleted.size)
        assertEquals(1, ret.unchanged.size)

        assertEquals(1, ret.unchanged[0].lineIn1)
        assertEquals(0, ret.unchanged[0].lineIn2)
        assertEquals(-1, ret.added[0].lineIn1)
        assertEquals(1, ret.added[0].lineIn2)
        assertEquals(0, ret.deleted[0].lineIn1)
        assertEquals(-1, ret.deleted[0].lineIn2)
    }

    @Test
    fun findLCSTest3() {
        val ret = findLCS(listOf("abacaba", "baca", "aoao", "baca"), listOf("baca", "baca"))
        assertEquals(0, ret.added.size)
        assertEquals(2, ret.deleted.size)
        assertEquals(2, ret.unchanged.size)

        assertEquals(1, ret.unchanged[0].lineIn1)
        assertEquals(0, ret.unchanged[0].lineIn2)
        assertEquals(3, ret.unchanged[1].lineIn1)
        assertEquals(1, ret.unchanged[1].lineIn2)
        assertEquals(0, ret.deleted[0].lineIn1)
        assertEquals(-1, ret.deleted[0].lineIn2)
        assertEquals(2, ret.deleted[1].lineIn1)
        assertEquals(-1, ret.deleted[1].lineIn2)
    }

    @Test
    fun findLCSTest4() {
        val ret = findLCS(listOf("aaaaa", "bb", "c"), listOf("d", "eee"))
        assertEquals(2, ret.added.size)
        assertEquals(3, ret.deleted.size)
        assertEquals(0, ret.unchanged.size)

        assertEquals(0, ret.deleted[0].lineIn1)
        assertEquals(-1, ret.deleted[0].lineIn2)
        assertEquals(1, ret.deleted[1].lineIn1)
        assertEquals(-1, ret.deleted[1].lineIn2)
        assertEquals(2, ret.deleted[2].lineIn1)
        assertEquals(-1, ret.deleted[2].lineIn2)
        assertEquals(-1, ret.added[0].lineIn1)
        assertEquals(0, ret.added[0].lineIn2)
        assertEquals(-1, ret.added[1].lineIn1)
        assertEquals(1, ret.added[1].lineIn2)
    }
}
