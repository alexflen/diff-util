/**
 * Дата класс для хранения номеров строки в каждом из двух сравниваемых файлов
 */
data class LineNumbers(val lineIn1: Int, val lineIn2: Int)

/**
 * Дата класс для хранения списка номеров строк для каждого статуса строк: добавленных, удаленных, не измененных
 */
data class LineInfo(val added: List<LineNumbers>, val deleted: List<LineNumbers>, val unchanged: List<LineNumbers>)

/**
 * Дата класс для хранения списков строк каждого из двух сравниваемых файлов
 */
data class FileLines(val file1Lines: List<String>, val file2Lines: List<String>)

/**
 * Функция, которая обрабатывает возврат функции findLCS и выводит результат работы программы с использованием
 * функции outputLine для обработки и вывода отдельных строк
 *
 * @param[output] -- возврат функции findLCS; строки разделены по статусу на списки: добавленные, удаленные, не измененные
 * @param[showOpts] -- параметры того, строки какого статуса будут отображены, в 3-битном виде
 * @param[hideOpts] -- параметры того, какая информация будет скрыта, в 3-битном виде
 * @param[outputFileName] -- имя файла, куда нужно вывести строку. если это имя файла является пустой строкой,
 * вывод нужно производить в консоль
 * @param[maxNumLen1] -- максимальная длина номера строки в первом файле. используется для выравнивания вывода
 * @param[maxNumLen2] -- максимальная длина номера строки во втором файле. используется для выравнивания вывода
 * @param[file1Lines] -- список строк первого файла
 * @param[file2Lines] -- список строк второго файла
 */
fun outputAnswer(output: LineInfo, showOpts: Int, hideOpts: Int, outputFileName: String,
                 maxNumLen1: Int, maxNumLen2: Int, file1Lines: List<String>, file2Lines: List<String>) {
    val sizeAdded = output.added.size
    val sizeDeleted = output.deleted.size
    val sizeUnchanged = output.unchanged.size
    // если не нужно показывать, просто можно сказать указателем, что уже все показали, и показывать ничего не нужно
    var iterAdd = when { // указатель на строку в добавленных
        ((showOpts shr 0) and 1) == 1 -> 0
        else -> sizeAdded
    }
    var iterDel = when { // указатель на строку в удаленных
        ((showOpts shr 1) and 1) == 1 -> 0
        else -> sizeDeleted
    }
    var iterUnc = 0 // указатель на строку в неизменных, так как он главный, для сохранения порядка остальных строк
    // по нему нужно проходиться

    // будем использовать метод трех указателей с главным списком неизменных строк, между неизмененными строками
    // будем выводить сначала удаления, потом добавления
    while (iterUnc != sizeUnchanged) {
        while (iterDel != sizeDeleted && output.deleted[iterDel].lineIn1 < output.unchanged[iterUnc].lineIn1) {
            outputLine(output.deleted[iterDel], file1Lines[output.deleted[iterDel].lineIn1], hideOpts,
                outputFileName, maxNumLen1, maxNumLen2)
            iterDel++
        }
        while (iterAdd != sizeAdded && output.added[iterAdd].lineIn2 < output.unchanged[iterUnc].lineIn2) {
            outputLine(output.added[iterAdd], file2Lines[output.added[iterAdd].lineIn2], hideOpts,
                outputFileName, maxNumLen1, maxNumLen2)
            iterAdd++
        }
        if ((showOpts shr 2) == 1) { // нужно выводить неизмененные строки
            outputLine(
                output.unchanged[iterUnc], file1Lines[output.unchanged[iterUnc].lineIn1], hideOpts,
                outputFileName, maxNumLen1, maxNumLen2)
        }
        iterUnc++
    }

    // неизменные строки могли закончиться, а удаленные и добавленные -- нет
    while (iterDel != sizeDeleted) {
        outputLine(output.deleted[iterDel], file1Lines[output.deleted[iterDel].lineIn1], hideOpts,
            outputFileName, maxNumLen1, maxNumLen2)
        iterDel++
    }

    while (iterAdd != sizeAdded) {
        outputLine(output.added[iterAdd], file2Lines[output.added[iterAdd].lineIn2], hideOpts,
            outputFileName, maxNumLen1, maxNumLen2)
        iterAdd++
    }

    if (((hideOpts shr 0) and 1) == 0) {  // не нужно скрывать информацию
        writeAnywhere("\n", outputFileName)
        writeAnywhere("Добавлено строк $sizeAdded, удалено строк $sizeDeleted, не изменено строк $sizeUnchanged\n",
            outputFileName)
    }
}

/**
 * Функция ищет НОП по строкам двух сравниваемых файлов, и возвращает обработанный результат в виде разделения всех
 * строк на три статуса: добавленные, удаленные, не измененные
 *
 * @param[file1Lines] -- список строк первого файла
 * @param[file2Lines] -- список строк второго файла
 * @param[ignoreLines] -- показатель того, нужно ли игнорировать пустые строки при сравнении строк файлов.
 * равен true, если нужно, и false, если не нужно.
 *
 * @return дата класс LineInfo, внутри которого лежат три списка, в котором все строки файлов разделены по статуса
 */
fun findLCS(file1Lines: List<String>, file2Lines: List<String>, ignoreLines: Boolean): LineInfo {
    // возвращает информацию о строках: про каждую строку будет известен ее номер в файле 1 и файле 2 (если она есть)
    val numLines1 = file1Lines.size
    val numLines2 = file2Lines.size
    val dp = Array(numLines1 + 1) { Array(numLines2 + 1) { 0 } }  // динамика будет dp[i][j] -- LCS
    // если взяли первые i строк первого и первые j строк второго; ответ в dp[numLines1][numLines2]
    val hashedFile1: MutableList<Int> = mutableListOf()
    val hashedFile2: MutableList<Int> = mutableListOf()
    // вместо строк будем сравнивать хэши
    repeat(numLines1) {
        hashedFile1.add(file1Lines[it].hashCode())
    }
    repeat(numLines2) {
        hashedFile2.add(file2Lines[it].hashCode())
    }

    repeat(numLines1) { num1 ->
        repeat(numLines2) { num2 ->
            when {
                // или пропуск пустых строк, или неравенство
                (ignoreLines && (file1Lines[num1].isEmpty() || file2Lines[num2].isEmpty())) ||
                        hashedFile1[num1] != hashedFile2[num2] -> dp[num1 + 1][num2 + 1] = dp[num1][num2 + 1].coerceAtLeast(dp[num1 + 1][num2])
                else -> dp[num1 + 1][num2 + 1] = dp[num1][num2] + 1
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
            if (!ignoreLines || file1Lines[num1 - 1].isNotEmpty())  // если строка пустая, и нужно игнорировать
                deleted.add(LineNumbers(num1 - 1, -1)) // так как мы не пришли по диагонали,
            // этой строки нет в НОП -> она удалена (она из первого файла)
            num1--
        } else if (dp[num1][num2] == dp[num1][num2 - 1]) {
            if (!ignoreLines || file2Lines[num2 - 1].isNotEmpty())  // если строка пустая, и нужно игнорировать
                added.add(LineNumbers(-1, num2 - 1)) //аналогично, этой строки нет в НОП -> добавлена
            num2--
        } else {
            unchanged.add(LineNumbers(num1 - 1, num2 - 1)) // значит, мы пришли по диагонали -> строки есть
            num1--
            num2--
        }
    }
    while (num1 > 0) { // некоторые строки не были добавлены, если while вышел по num2 = 0
        if (!ignoreLines || file1Lines[num1 - 1].isNotEmpty())  // если строка пустая, и нужно игнорировать
            deleted.add(LineNumbers(num1 - 1, -1))
        num1--
    }
    while (num2 > 0) { // некоторые строки не были добавлены, если while вышел по num1 = 0
        if (!ignoreLines || file2Lines[num2 - 1].isNotEmpty())  // если строка пустая, и нужно игнорировать
            added.add(LineNumbers(-1, num2 - 1))
        num2--
    }
    added.reverse()
    deleted.reverse()
    unchanged.reverse()  // строки записывали в обратном порядке, т.к. шли с конца в восстановлении
    return LineInfo(added.toList(), deleted.toList(), unchanged.toList())
}

/**
 * Функция запускает все остальные функции, а также обрабатывает ввод из параметров компиляции
 *
 * @param[args] -- ввод из параметров компиляции
 */
fun main(args: Array<String>) {
    val commandList: List<String> = if (args.isNotEmpty()) {
        args.toList()
    } else {
        parseInput()
    }

    if (commandList.isEmpty()) { // строка ввода пустая, следует завершить выполнение программы
        return
    }

    if (processCommand(commandList) == 1) { // в процессе обработки произошла ошибка
        return
    }
}
