/**
 * Функция выводит в консоль сообщение об ошибке
 *
 * @param[error] отвечает за строковый код ошибки, который и влияет на вывод
 * @param[opt] или передает имя опции, или передает имя файла для отображения в тексте ошибки
 */
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

/**
 * Функция проверяет, в правильном ли формате указаны параметры опции, требующей параметры
 *
 * @param[opt] передает название опции, для которой происходит проверка
 * @param[currOpt] является строкой, содержащей и объявление опции, и ее параметры,
 * эта строка и проверяется на корректность.
 * @return false, если что-то не так, при этом до возврата функция запустит вывод ошибки с соответствующим кодом
 * @return true, если формат соблюден.
 */
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

/**
 * Функция считает длину целого числа
 *
 * @param[number] -- число, для которого нужно посчитать длину
 * @return длину числа
 */
fun numberLength(number: Int): Int {
    return number.toString().length
}