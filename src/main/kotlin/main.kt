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

fun printHelp() {
    TODO()
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

fun outputAnswer(showOpts: Int, hideOpts: Int, ignoreLines: Boolean, outputFile: String?, addWrite: Boolean) {
    TODO()
}

fun processCommand(commandList: List<String>): Int { // возвращает 0, если выполнилось без ошибок, 1 иначе
    if (commandList.size == 1) {
        errNotEnoughArgs()
        return 1
    }

    val file1Name = commandList[0]
    val file2Name = commandList[1]
    val file1 = File(file1Name)
    val file2 = File(file2Name)
    if (!file1.exists()) {
        errWrongInputFile(file1Name)
        return 1
    }
    if (!file2.exists()) {
        errWrongInputFile(file2Name)
        return 1
    }

    var showOpts = 7
    var hideOpts = 0
    var ignoreLines = false
    var outputFile = ""
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
            if (currOpt.length < 4) { // пустое имя выходного файла; у опции должны быть параметры
                errNoParameters("-$opt")
                return 1
            }
            if (currOpt[2] != '=') {  // неправильный формат (без =)
                errFormatEqual()
                return 1
            }
            outputFile = currOpt.substring(3)
            addWrite = opt // w - запись, W - дозапись
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
