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

fun parseInput(): Array<String> {
    var input = readLine()
    if (input != null) {
        input += ' '    // чтобы последняя строка корректно обработалась
        var tempStr = ""
        var result = emptyArray<String>()
        for (symb in input) {
            if (symb == ' ') {
                if (tempStr.isNotEmpty()) {
                    result += tempStr
                    tempStr = ""
                }
            } else {
                tempStr += symb
            }
        }
        return result
    } else {
        errNotEnoughArgs()
        return emptyArray()
    }
}


fun processCommand(commandList: Array<String>) {
    TODO()
}

fun outputAnswer(showOpts: Int, hideOpts: Int, ignoreLines: Boolean, outputFile: String?) {
    TODO()
}

// file1.txt file2.txt -s=uad -h=ins -i -w=answer.txt

fun main(args: Array<String>) {
    val commandList: Array<String> = if (args.isNotEmpty()) {
        args.copyOf()
    } else {
        parseInput()
    }


}
