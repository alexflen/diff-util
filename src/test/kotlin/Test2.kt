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
        main(arrayOf("testfiles\\02_file1.txt", "testfiles\\02_file2.txt", "-i", "-s=ua"))
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

    @Test
    fun mainTest5() {
        main(arrayOf("testfiles\\02_file228.txt", "testfiles\\02_file2.txt", "-i", "-s=ua"))
        val expectedLine = "Неверное имя файла: testfiles\\02_file228.txt"
        val actualLine = stream.toString().trimEnd().lines().last()
        assertEquals(expectedLine, actualLine)
    }

    @Test
    fun mainTest6() {
        main(arrayOf("testfiles\\02_file2.txt", "testfiles\\02_file2.txt", "-p", "-s=ua"))
        val expectedLine = "Неизвестная опция -p"
        val actualLine = stream.toString().trimEnd().lines().last()
        assertEquals(expectedLine, actualLine)
    }

    @Test
    fun mainTest7() {
        main(arrayOf("testfiles\\02_file2.txt", "testfiles\\02_file2.txt", "-i", "-s=uab"))
        val expectedLine = "Неизвестный параметр для опции -s"
        val actualLine = stream.toString().trimEnd().lines().last()
        assertEquals(expectedLine, actualLine)
    }

    @Test
    fun mainTest8() {
        main(arrayOf("testfiles\\02_file2.txt"))
        val expectedLine = "Недостаточно параметров для вызова функции"
        val actualLine = stream.toString().trimEnd().lines().last()
        assertEquals(expectedLine, actualLine)
    }
}