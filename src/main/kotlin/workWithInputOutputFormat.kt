import java.io.File

/**
 * Функция, которая приводит строку к нужному формату, а затем ее выводит с помощью функции writeAnywhere
 *
 * @param[lineNum] -- номера строки, которую сейчас нужно обработать, в сравниваемых файлах
 * @param[line] -- сама строка
 * @param[hideOpts] -- параметры скрываемой информации в 3-битном формате
 * @param[outputFileName] -- имя файла, куда нужно вывести строку. если это имя файла является пустой строкой,
 * вывод нужно производить в консоль
 * @param[maxNumLen1] -- максимальная длина номера строки в первом файле. используется для выравнивания вывода
 * @param[maxNumLen2] -- максимальная длина номера строки во втором файле. используется для выравнивания вывода
 */
fun outputLine(lineNum: LineNumbers, line: String, hideOpts: Int,
               outputFileName: String, maxNumLen1: Int, maxNumLen2: Int) {
    var toOutput = ""
    if (((hideOpts shr 1) and 1) == 0) {  // не нужно прятать номера строк
        if (lineNum.lineIn1 != -1) {  // есть в первом файле
            val lineNum1Str = (lineNum.lineIn1 + 1).toString()  // чтобы с 1 нумерация
            toOutput += "(${lineNum1Str.padStart(maxNumLen1)})"
        } else {
            toOutput += "".padStart(maxNumLen1 + 2) // добавить длину двух скобок
        }
        if (lineNum.lineIn2 != -1) {  // есть во втором файле
            val lineNum2Str = (lineNum.lineIn2 + 1).toString()  // чтобы с 1 нумерация
            toOutput += "[${lineNum2Str.padStart(maxNumLen2)}]"
        } else {
            toOutput += "".padStart(maxNumLen2 + 2)  // добавить длину двух скобок
        }
        toOutput += "  "// добавим разделение между номерами строк и оставшимся
    }
    if (((hideOpts shr 2) and 1) == 0) {  // не нужно прятать статус изменений
        if (lineNum.lineIn1 == -1) {  // добавление
            toOutput += "+"
        } else if (lineNum.lineIn2 == -1) { // удаление
            toOutput += "-"
        } else { // без изменений
            toOutput += "|"
        }
        toOutput += "  "
    }

    writeAnywhere("$toOutput$line\n", outputFileName)
}

/**
 * функция, обрабатывающая команду: она понимает, что делают опции
 * и передает полученные параметры в функцию outputAnswer
 *
 * @param[commandList] -- список параметров команды
 * @return 0, если команды обработаны успешно, и 1, если в процессе произошла ошибка, при этои
 * до возврата вызывает сообщение об ошибке
 */
fun processCommand(commandList: List<String>): Int {
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

    val options: Map<Char, String> = mapOf('h' to "ins", 's' to "adu", 'i' to "", 'w' to "F", 'W' to "F")
    // Map показывает возможные варианты параметров команд, а также кодирует их для 3-битного представления параметров
    for (it in (2 until commandList.size)) { // проходимся по опциям
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
            for (param in currOpt.substring(3)) {  // проходимся по параметрам
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

    val returnedLCSData = findLCS(file1Lines, file2Lines, ignoreLines)
    // создадим файл вывода, если его нет
    if (outputFile != "") {
        val fileForOutput = File(outputFile)
        if (!fileForOutput.isFile) {
            fileForOutput.createNewFile()
        }
        if (addWrite == 'w') {  // сотрем файл, если нужно, а дальше будет дозапись
            fileForOutput.writeText("")
        }
    }
    outputAnswer(returnedLCSData, showOpts, hideOpts, outputFile,
        numberLength(file1Lines.size), numberLength(file2Lines.size), file1Lines, file2Lines)
    // максимальная длина номера строки меньше либо равна длине количества строк в файле
    return 0
}
