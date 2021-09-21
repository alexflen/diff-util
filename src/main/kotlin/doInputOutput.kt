import java.io.File

/**
 * Функция читает и разделяет строку из консольного ввода по пробельным символам
 * (несколько пробелов считаются как один разделитель)
 *
 * @return список строк, полученных после разбиения прочитанной строки по пробельным символам
 * @return пустой список, если считанная строка пустая, при этом до возврата вызывает сообщение об ошибке
 */
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

/**
 * Функция вывода строки куда-либо
 *
 * @param[line] -- строка для вывода
 * @param[outputFileName] -- имя файла, куда нужно вывести строку. если это имя файла является пустой строкой,
 * вывод нужно производить в консоль
 */
fun writeAnywhere(line: String, outputFileName: String) {
    if (outputFileName == "") {
        print(line)
    } else {
        val outputFile = File(outputFileName)
        outputFile.appendText(line)
    }
}

/**
 * Функция читает два файла, параллельно обрабатывая ошибки
 *
 * @param[file1Name] -- имя (путь) первого файла
 * @param[file2Name] -- имя (путь) второго файла
 *
 * @return дата класс FileLines, в котором содержатся два списка строк файлов. возвращает дата класс из двух пустых списков,
 * если произошла какая-то ошибка, при этом до возврата вызывает сообщение об ошибке
 */
fun readFiles(file1Name: String, file2Name: String): FileLines {
    val file1 = File(file1Name)
    val file2 = File(file2Name)
    if (!file1.isFile) {  // не существует файла с именем file1Name
        outputError("WrongInputFile", file1Name)
        return FileLines(emptyList(), emptyList())
    }
    if (!file2.isFile) {  // не существует файла с именем file2Name
        outputError("WrongInputFile", file2Name)
        return FileLines(emptyList(), emptyList())
    }

    val result = FileLines(file1.readLines(), file2.readLines())  // сначала список строк первого, потом второго
    if (result.file1Lines.isEmpty() && result.file2Lines.isEmpty()) {  // оба файла пусты
        outputError("BothEmpty")
    }

    return result
}
