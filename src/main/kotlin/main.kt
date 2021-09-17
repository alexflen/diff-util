import java.io.File

fun terminate() {
    TODO()
}

fun errNotEnoughArgs() {
    println("Недостаточно параметров для вызова функции")
    terminate()
}

fun errUnknownOption(opt: Char) {
    println("Неизвестная опция $opt")
    terminate()
}

fun errWrongInputFile(fileName: String) {
    println("Неверное имя файла: $fileName")
    terminate()
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


fun processCommand(commandList: List<String>) {
    TODO()
}

fun outputAnswer(showOpts: Int, hideOpts: Int, ignoreLines: Boolean, outputFile: String?, addWrite: Boolean) {
    TODO()
}

// file1.txt file2.txt -s=uad -h=ins -i -w/W=answer.txt

fun main(args: Array<String>) {
    val commandList: List<String> = if (args.isNotEmpty()) {
        args.toList()
    } else {
        parseInput()
    }


}
