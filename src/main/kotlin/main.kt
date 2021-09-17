import jdk.internal.jimage.ImageStringsReader.hashCode
import java.io.File
import kotlin.system.exitProcess

fun errNotEnoughArgs() {
    println("Недостаточно параметров для вызова функции")
}

fun errUnknownOption(opt: String) {
    println("Неизвестная опция $opt")
}

fun errWrongInputFile(fileName: String) {
    println("Неверное имя файла: $fileName")
}

fun errOptsMustBegin() {
    println("Неверный формат: опции должны начинаться с -")
}

fun errFormatEqual() {
    println("Неверный формат: параметры опций должны идти после =")
}

fun errUnknownParam(opt: String) {
    println("Неизвестный параметр для опции $opt")
}

fun errNoParameters(opt: String) {
    println("У опции $opt должны быть параметры, но их нет")
}

fun errBothEmpty() {
    println("Нечего сравнивать: оба файла пусты")
}

fun printHelp() {
    TODO()
}

fun checkIfOkFormatParams(opt: Char, currOpt: String): Boolean {  // true, если все хорошо, false, если что-то не так
    if (currOpt.length == 2 || (currOpt.length == 3 && currOpt[2] == '=')) { // у опции должны быть параметры
        errNoParameters("-$opt")
        return false
    }
    if (currOpt[2] != '=') {  // неправильный формат (без =)
        errFormatEqual()
        return false
    }
    return true
}

fun parseInput(): List<String> {
    var input = readLine()
    if (input != null) {
        input += ' '    // чтобы последняя строка корректно обработалась
        var tempStr = ""
        val result: MutableList<String> = mutableListOf()
        for (symb in input) {
            if (symb == ' ') {
                if (tempStr.isNotEmpty()) {
                    result.add(tempStr)
                    tempStr = ""
                }
            } else {
                tempStr += symb
            }
        }
        return result.toList()
    } else {
        errNotEnoughArgs()
        return emptyList()
    }
}

class LineNumbers(val lineIn1: Int, val lineIn2: Int)

fun outputAnswer(showOpts: Int, hideOpts: Int, ignoreLines: Boolean, outputFile: String?, addWrite: Boolean) {
    TODO()
}

fun findLCS(file1Lines: List<String>, file2Lines: List<String>): List<List<LineNumbers>> {
    val numLines1 = file1Lines.size
    val numLines2 = file2Lines.size
    var dp = Array(2) { Array(numLines2) {0} }  // первое измерение можно сделать не размера num1Lines, а 2
    // так как используется только предыдущий слой при подсчете динамики
    val hashedFile1: MutableList<Int> = mutableListOf()
    val hashedFile2: MutableList<Int> = mutableListOf()
    // вместо строк будем сравнивать хэши
    repeat(numLines1) {
        hashedFile1.add(hashCode(file1Lines[it]))
    }
    repeat(numLines2) {
        hashedFile2.add(hashCode(file2Lines[it]))
    }
    TODO()
}

fun readFiles(file1Name: String, file2Name: String): Pair<List<String>, List<String>> {
    val file1 = File(file1Name)
    val file2 = File(file2Name)
    if (!file1.exists()) {  // не существует файла с именем file1Name
        errWrongInputFile(file1Name)
        return Pair(emptyList(), emptyList())
    }
    if (!file2.exists()) {  // не существует файла с именем file2Name
        errWrongInputFile(file2Name)
        return Pair(emptyList(), emptyList())
    }

    val result = Pair(file1.readLines(), file2.readLines())  // сначала список строк первого, потом второго
    if (result.first.isEmpty() && result.second.isEmpty()) {  // оба файла пусты
        errBothEmpty()
    }

    return result
}

fun processCommand(commandList: List<String>): Int { // возвращает 0, если выполнилось без ошибок, 1 иначе
    if (commandList.size == 1) {
        errNotEnoughArgs()
        return 1
    }

    val file1Name = commandList[0]
    val file2Name = commandList[1]
    val (file1Lines, file2Lines) = readFiles(file1Name, file2Name)
    if (file1Lines.isEmpty() && file2Lines.isEmpty()) {
        return 1
    }

    var showOpts = 7 // по умолчанию все строки показываются
    var hideOpts = 0 // по умолчанию вся информация показывается
    var ignoreLines = false // по умолчанию пустые строки не пропускаются
    var outputFile = "" // по умолчанию вывод в консоль
    var addWrite = '-'

    var outputLines: MutableList<String>
    val options: Map<Char, String> = mapOf('h' to "ins", 's' to "adu", 'i' to "", 'w' to "F", 'W' to "F")
    for (it in (2 until commandList.size)) {
        val currOpt = commandList[it]
        if (currOpt[0] != '-') {
            errOptsMustBegin()
            return 1
        }

        if (currOpt.length == 1) {
            errUnknownOption(currOpt)
        }

        val opt = currOpt[1]
        if (!options.containsKey(opt)) {
            errUnknownOption("-$opt")
            return 1
        }
        if (opt == 'i') {   // опция ignore empty lines не требует параметров
            if (currOpt.length > 2) {
                errUnknownParam("-$opt")
                return 1
            } else {
                ignoreLines = true
            }
        } else if (options[opt] == "F") {   // добавление файла вывода
            // учитывается только последний вызов w или W
            if (!checkIfOkFormatParams(opt, currOpt)) {
                return 1
            }
            outputFile = currOpt.substring(3)
            addWrite = opt // w - запись, W - дозапись
        } else {  // show or hide option
            // учитывается только последний вызов
            if (!checkIfOkFormatParams(opt, currOpt)) {
                return 1
            }
            when (opt) {
                's' -> showOpts = 0
                'h' -> hideOpts = 0
            }
            for (param in currOpt.substring(3)) {
                val symbIndex = options[opt]!!.indexOf(param)
                if (symbIndex == -1) {
                    errUnknownParam("-$opt")
                    return 1
                }
                when (opt) {
                    's' -> showOpts = (showOpts or (1 shl symbIndex))
                    'h' -> hideOpts = (hideOpts or (1 shl symbIndex))
                }
            }
        }
    }



    return 0
}

// file1.txt file2.txt -s=uad -h=ins -i -w/W=answer.txt

fun main(args: Array<String>) {
    val commandList: List<String> = if (args.isNotEmpty()) {
        args.toList()
    } else {
        parseInput()
    }

    if (commandList.isEmpty()) { // строка ввода пустая, следует завершить выполнение программы
        return
    }


}
