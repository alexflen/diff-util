import kotlin.test.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

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
}
