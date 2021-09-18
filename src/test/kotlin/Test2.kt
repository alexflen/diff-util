import kotlin.test.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

internal class Test2 {
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

    @Test
    fun mainTest1() {
        System.setIn(ByteArrayInputStream(("testfiles\\01_file1.txt testfiles\\01_file2.txt -w=temp\\answer.txt").toByteArray()))
        main(arrayOf())
        val expectedLines = File("testfiles\\01_answ.txt").readLines()
        val actualLines = File("temp\\answer.txt").readLines()
        assertEquals(expectedLines, actualLines)
    }

    @Test
    fun mainTest2() {
        System.setIn(ByteArrayInputStream(("testfiles\\02_file1.txt testfiles\\02_file2.txt -i -s=ua").toByteArray()))
        main(arrayOf())
        val expectedLines = File("testfiles\\02_answ.txt").readLines()
        val actualLines = stream.toString().trimEnd().lines()
        assertEquals(expectedLines, actualLines)
    }

    @Test
    fun mainTest3() {
        System.setIn(ByteArrayInputStream(("testfiles\\03_file1.txt testfiles\\03_file2.txt -s=u -h=ni").toByteArray()))
        main(arrayOf())
        val expectedLines = File("testfiles\\03_answ.txt").readLines()
        val actualLines = stream.toString().trimEnd().lines()
        assertEquals(expectedLines, actualLines)
    }

    @Test
    fun mainTest4() {
        System.setIn(ByteArrayInputStream(("testfiles\\04_file1.txt testfiles\\04_file2.txt -s=ud -h=ni -w=temp\\answer.txt").toByteArray()))
        main(arrayOf())
        val expectedLines = File("testfiles\\04_answ.txt").readLines()
        val actualLines = File("temp\\answer.txt").readLines()
        assertEquals(expectedLines, actualLines)
    }
/*
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
    } */
}