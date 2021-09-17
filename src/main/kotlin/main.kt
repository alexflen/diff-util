import java.io.File

fun outputError(error: String, opt: String = "") {  // функция выводит сообщение об ошибке
    when (error) {
        "NotEnoughArgs"     -> println("Недостаточно параметров для вызова функции")
        "UnknownOption"     -> println("Неизвестная опция $opt")
        "WrongInputFile"    -> println("Неверное имя файла: $opt")
        "OptsMustBegin"     -> println("Неверный формат: опции должны начинаться с -")
        "FormatEqual"       -> println("Неверный формат: параметры опций должны идти после =")
        "UnknownParam"      -> println("Неизвестный параметр для опции $opt")
        "NoParameters"      -> println("У опции $opt должны быть параметры, но их нет")
        "BothEmpty"         -> println("Нечего сравнивать: оба файла пусты")
        else                -> println("Неизвестная ошибка")
    }
}

fun printHelp() {
    TODO()
}

fun checkIfOkFormatParams(opt: Char, currOpt: String): Boolean {  // true, если все хорошо, false, если что-то не так
    if (currOpt.length == 2 || (currOpt.length == 3 && currOpt[2] == '=')) { // у опции должны быть параметры
        outputError("NoParameters", "-$opt")
        return false
    }
    if (currOpt[2] != '=') {  // неправильный формат (без =)
        outputError("FormatEqual")
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
        outputError("NotEnoughArgs")
        return emptyList()
    }
}

class LineNumbers(val lineIn1: Int, val lineIn2: Int)
class LineInfo(val added: List<LineNumbers>, val deleted: List<LineNumbers>, val unchanged: List<LineNumbers>)

fun outputAnswer(showOpts: Int, hideOpts: Int, ignoreLines: Boolean, outputFile: String?, addWrite: Boolean) {
    TODO()
}

fun myHash(str: String): Int {
    val mod: Long = 1000000009 // 10^9 + 9
    val p: Long = 263
    var result: Long = 0
    for (symb in str) {
        result = (result * p + symb.code) % mod
    }
    return result.toInt()
}

fun findLCS(file1Lines: List<String>, file2Lines: List<String>): LineInfo {
    // возвращает информацию о строках: про каждую строку будет известен ее номер в файле 1 и файле 2 (если она есть)
    val numLines1 = file1Lines.size
    val numLines2 = file2Lines.size
    val dp = Array(numLines1 + 1) { Array(numLines2 + 1) {0} }  // динамика будет dp[i][j] -- LCS
    // если взяли первые i строк первого и первые j строк второго; ответ в dp[numLines1][numLines2]
    val hashedFile1: MutableList<Int> = mutableListOf()
    val hashedFile2: MutableList<Int> = mutableListOf()
    // вместо строк будем сравнивать хэши
    repeat(numLines1) {
        hashedFile1.add(myHash(file1Lines[it]))
    }
    repeat(numLines2) {
        hashedFile2.add(myHash(file2Lines[it]))
    }

    repeat(numLines1) { num1 ->
        repeat(numLines2) { num2 ->
            when {
                file1Lines[num1] == file2Lines[num2] -> dp[num1 + 1][num2 + 1] = dp[num1][num2] + 1
                else -> dp[num1 + 1][num2 + 1] = dp[num1][num2 + 1].coerceAtLeast(dp[num1 + 1][num2])
            }
        }
    }

    val added: MutableList<LineNumbers> = mutableListOf()
    val deleted: MutableList<LineNumbers> = mutableListOf()
    val unchanged: MutableList<LineNumbers> = mutableListOf()
    // восстановление ответа: если пришли из клеток, соседних по стороне, то вернемся в них
    // иначе мы добавили две равные строки (мы рассматриваем случаи именно в таком порядке
    var num1 = numLines1
    var num2 = numLines2 // ответ динамики в dp[num1][num2]
    while (num1 > 0 && num2 > 0) {  // нулевая строка и нулевой столбец фиктивные
        if (dp[num1][num2] == dp[num1 - 1][num2]) {
            deleted.add(LineNumbers(num1 - 1, -1)) // так как мы не пришли по диагонали,
            // этой строки нет в НОП -> она удалена (она из первого файла)
            num1--
        } else if (dp[num1][num2] == dp[num1][num2 - 1]) {
            added.add(LineNumbers(-1, num2 - 1)) //аналогично, этой строки нет в НОП -> добавлена
            num2--
        } else {
            unchanged.add(LineNumbers(num1 - 1, num2 - 1)) // значит, мы пришли по диагонали -> строки есть
            num1--
            num2--
        }
    }
    while (num1 > 0) { // некоторые строки не были добавлены, если while вышел по num2 = 0
        deleted.add(LineNumbers(num1 - 1, -1))
        num1--
    }
    while (num2 > 0) { // некоторые строки не были добавлены, если while вышел по num1 = 0
        added.add(LineNumbers(-1, num2 - 1))
        num2--
    }
    added.reverse()
    deleted.reverse()
    unchanged.reverse()  // строки записывали в обратном порядке, т.к. шли с конца в восстановлении
    return LineInfo(added.toList(), deleted.toList(), unchanged.toList())
}

fun readFiles(file1Name: String, file2Name: String): Pair<List<String>, List<String>> {
    val file1 = File(file1Name)
    val file2 = File(file2Name)
    if (!file1.exists()) {  // не существует файла с именем file1Name
        outputError("WrongInputFile", file1Name)
        return Pair(emptyList(), emptyList())
    }
    if (!file2.exists()) {  // не существует файла с именем file2Name
        outputError("WrongInputFile", file2Name)
        return Pair(emptyList(), emptyList())
    }

    val result = Pair(file1.readLines(), file2.readLines())  // сначала список строк первого, потом второго
    if (result.first.isEmpty() && result.second.isEmpty()) {  // оба файла пусты
        outputError("BothEmpty")
    }

    return result
}

fun processCommand(commandList: List<String>): Int { // возвращает 0, если выполнилось без ошибок, 1 иначе
    if (commandList.size == 1) {  // в команду нужно передать хотя бы два аргумента: названия двух файлов
        outputError("NotEnoughArgs")
        return 1
    }

    val file1Name = commandList[0]
    val file2Name = commandList[1]
    val (file1Lines, file2Lines) = readFiles(file1Name, file2Name)  // списки строк для файлов
    if (file1Lines.isEmpty() && file2Lines.isEmpty()) {   // функция чтения файлов выдала исключение
        return 1
    }

    var showOpts = 7 // по умолчанию все строки показываются
    var hideOpts = 0 // по умолчанию вся информация показывается
    var ignoreLines = false // по умолчанию пустые строки не пропускаются
    var outputFile = "" // по умолчанию вывод в консоль
    var addWrite = '-' // по умолчанию вывод в консоль

    var outputLines: MutableList<String>
    val options: Map<Char, String> = mapOf('h' to "ins", 's' to "adu", 'i' to "", 'w' to "F", 'W' to "F")
    for (it in (2 until commandList.size)) {
        val currOpt = commandList[it]
        if (currOpt[0] != '-') {
            outputError("OptsMustBegin")
            return 1
        }

        if (currOpt.length == 1) {
            outputError("UnknownOption", currOpt)
        }

        val opt = currOpt[1]
        if (!options.containsKey(opt)) {
            outputError("UnknownOption", "-$opt")
            return 1
        }
        if (opt == 'i') {   // опция ignore empty lines не требует параметров
            if (currOpt.length > 2) {
                outputError("UnknownParam", "-$opt")
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
                    outputError("UnknownParam", "-$opt")
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
