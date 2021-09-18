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
        val ret = findLCS(listOf("abacaba", "baca"), listOf("abacaba", "baca"), false)
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
        val ret = findLCS(listOf("abacaba", "baca"), listOf("baca", "baca"), false)
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
        val ret = findLCS(listOf("abacaba", "baca", "aoao", "baca"), listOf("baca", "baca"), false)
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
        val ret = findLCS(listOf("aaaaa", "bb", "c"), listOf("d", "eee"), false)
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

    @Test
    fun findLCSTest5() {
        val ret = findLCS(listOf("aaaaa", "", "c"), listOf("", "eee"), true)
        assertEquals(1, ret.added.size)
        assertEquals(2, ret.deleted.size)
        assertEquals(0, ret.unchanged.size)

        assertEquals(0, ret.deleted[0].lineIn1)
        assertEquals(-1, ret.deleted[0].lineIn2)
        assertEquals(2, ret.deleted[1].lineIn1)
        assertEquals(-1, ret.deleted[1].lineIn2)
        assertEquals(-1, ret.added[0].lineIn1)
        assertEquals(1, ret.added[0].lineIn2)
    }

    @Test
    fun findLCSTest6() {
        val ret = findLCS(listOf("deleted", "", "unchanged", "", ""), listOf("", "", "", "unchanged"), true)
        assertEquals(0, ret.added.size)
        assertEquals(1, ret.deleted.size)
        assertEquals(1, ret.unchanged.size)

        assertEquals(0, ret.deleted[0].lineIn1)
        assertEquals(-1, ret.deleted[0].lineIn2)
        assertEquals(2, ret.unchanged[0].lineIn1)
        assertEquals(3, ret.unchanged[0].lineIn2)
    }

    @Test
    fun findLCSTest7() {
        val ret = findLCS(listOf("deleted", "", "unchanged", "", ""), listOf("", "", "", "unchanged"), false)
        assertEquals(1, ret.added.size)
        assertEquals(2, ret.deleted.size)
        assertEquals(3, ret.unchanged.size)

        assertEquals(0, ret.deleted[0].lineIn1)
        assertEquals(-1, ret.deleted[0].lineIn2)
        assertEquals(2, ret.deleted[1].lineIn1)
        assertEquals(-1, ret.deleted[1].lineIn2)
        assertEquals(1, ret.unchanged[0].lineIn1)
        assertEquals(0, ret.unchanged[0].lineIn2)
        assertEquals(3, ret.unchanged[1].lineIn1)
        assertEquals(1, ret.unchanged[1].lineIn2)
        assertEquals(4, ret.unchanged[2].lineIn1)
        assertEquals(2, ret.unchanged[2].lineIn2)
        assertEquals(-1, ret.added[0].lineIn1)
        assertEquals(3, ret.added[0].lineIn2)
    }

    @Test
    fun outputLineTest1() {
        outputLine(LineNumbers(0, 2), "line line", 0, "", '-', 2, 1)
        assertEquals("( 1)[3]  |  line line", stream.toString().trim().lines().last())
    }

    @Test
    fun outputLineTest2() {
        outputLine(LineNumbers(-1, 2), "line line", 0, "", '-', 2, 1)
        assertEquals("    [3]  +  line line", stream.toString().trimEnd().lines().last())
    }

    @Test
    fun outputLineTest3() {
        outputLine(LineNumbers(3, -1), "line line", 0, "", '-', 3, 1)
        assertEquals("(  4)     -  line line", stream.toString().trimEnd().lines().last())
    }

    @Test
    fun outputLineTest4() {
        outputLine(LineNumbers(3, -1), "line line", 2, "", '-', 3, 1)
        assertEquals("-  line line", stream.toString().trimEnd().lines().last())
    }

    @Test
    fun outputLineTest5() {
        outputLine(LineNumbers(100, 120), "line line", 7, "", '-', 4, 4)
        assertEquals("line line", stream.toString().trimEnd().lines().last())
    }

    @Test
    fun outputLineTest6() {
        outputLine(LineNumbers(10, 32), "line line", 5, "", '-', 2, 3)
        assertEquals("(11)[ 33]  line line", stream.toString().trim().lines().last())
    }

    @Test
    fun outputAnswerTest1() {
        outputAnswer(LineInfo(listOf(LineNumbers(-1, 1)), listOf(LineNumbers(0, -1), LineNumbers(2, -1)), listOf(LineNumbers(1, 0))), 7, 1,"", '-', 1, 1, listOf("line1", "line2", "line3"), listOf("line2", "line5"))
        assertEquals("(1)     -  line1\n(2)[1]  |  line2\n(3)     -  line3\n   [2]  +  line5", stream.toString().trimEnd())
    }

    @Test
    fun outputAnswerTest2() {
        outputAnswer(LineInfo(listOf(), listOf(LineNumbers(0, -1)), listOf(LineNumbers(1, 0), LineNumbers(2, 1))), 7, 0,"", '-', 1, 1, listOf("line1", "line2", "line3"), listOf("line2", "line3"))
        assertEquals("(1)     -  line1\n(2)[1]  |  line2\n(3)[2]  |  line3\n\nAdded 0 line(s), deleted 1 line(s), left unchanged 2 line(s)", stream.toString().trimEnd())
    }

    @Test
    fun outputAnswerTest3() {
        outputAnswer(LineInfo(listOf(), listOf(LineNumbers(0, -1)), listOf(LineNumbers(1, 0), LineNumbers(2, 1))), 3, 0,"", '-', 1, 1, listOf("line1", "line2", "line3"), listOf("line2", "line3"))
        assertEquals("(1)     -  line1\n\nAdded 0 line(s), deleted 1 line(s), left unchanged 2 line(s)", stream.toString().trimEnd())
    }

    @Test
    fun outputAnswerTest4() {
        outputAnswer(LineInfo(listOf(LineNumbers(-1, 1)), listOf(LineNumbers(0, -1), LineNumbers(2, -1)), listOf(LineNumbers(1, 0))), 4, 1,"", '-', 1, 1, listOf("line1", "line2", "line3"), listOf("line2", "line5"))
        assertEquals("(2)[1]  |  line2", stream.toString().trimEnd())
    }
}
