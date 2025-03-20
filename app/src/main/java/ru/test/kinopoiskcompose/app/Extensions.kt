package ru.test.kinopoiskcompose.app

import ru.test.kinopoiskcompose.data.Month
import ru.test.kinopoiskcompose.db.model.FilmWithDetailInfo


fun <T> List<T>.prepareToShow(size: Int): List<T> {
    val resultList = mutableListOf<T>()

    if (this.size <= size) {
        this.forEach { resultList.add(it) }
    } else {
        repeat(size) {
            resultList.add(this[it])
        }
    }
    resultList.add(this[0])
    return resultList.toList()
}


fun Int.converterInMonth(): String {
    var textMonth = ""
    if (this <= 0 || this > 12)
        textMonth = Month.AUGUST.name
    else
        Month.values().forEach { month ->
            if (this == month.count) textMonth = month.name
        }
    return textMonth
}

fun FilmWithDetailInfo.getStrCountriesLengthAge(): String {
    val result = mutableListOf<String?>()

    val countries = this.countries
    val resultCountries = if (countries != null) {
        if (countries.size == 1) {
            countries.first().country
        } else if (countries.size > 1) {
            countries.joinToString(", ") { it.country }
        } else {
            null
        }
    } else {
        null
    }
    result.add(resultCountries)

    val length = this.detailInfo?.length
    val resultLength = if (length != null) {
        val hours = length.div(60)
        val minutes = length.rem(60)
        "$hours ч $minutes мин"
    } else null
    result.add(resultLength)

    val resultAgeLimit = this.detailInfo?.ageLimit?.removePrefix("age")
    result.add("$resultAgeLimit+")

    return result.joinToString(", ")
}




