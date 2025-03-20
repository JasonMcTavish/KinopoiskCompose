package ru.test.kinopoiskcompose.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.test.kinopoiskcompose.db.model.FilmsShortInfo
import ru.test.kinopoiskcompose.db.model.NewFilmGenres
import ru.test.kinopoiskcompose.model.FilmShortInfoModel
import ru.test.kinopoiskcompose.model.GenresModel

@JsonClass(generateAdapter = true)
data class ResponsePremier(
    @Json(name = "items") val films: List<FilmPremier>,
    @Json(name = "total") val total: Int
)

@JsonClass(generateAdapter = true)
data class FilmPremier(
    @Json(name = "kinopoiskId") val filmId: Int,
    @Json(name = "nameRu") val nameRu: String,
    @Json(name = "nameEn") val nameEn: String,
    @Json(name = "posterUrlPreview") val posterUrlPreview: String,
    @Json(name = "posterUrl") val posterUrl: String,
    @Json(name = "genres") val genres: List<Genre>,
)

fun FilmPremier.convertForDbShortInfo(): FilmsShortInfo {
    return FilmsShortInfo(
        filmId = this.filmId,
        name = if (this.nameRu.isNotEmpty()) this.nameRu else if (this.nameEn.isNotEmpty()) this.nameEn else "",
        poster = this.posterUrlPreview,
        rating = null
    )
}

fun FilmPremier.convertForDbGenres(): List<NewFilmGenres> {
    return this.genres.map { genre ->
        NewFilmGenres(
            filmId = this.filmId,
            genre = genre.genre
        )
    }
}

fun FilmPremier.convertToModelShortInfo(): FilmShortInfoModel {
    return FilmShortInfoModel(
        filmId = this.filmId,
        name = if (this.nameRu.isNotEmpty()) this.nameRu else if (this.nameEn.isNotEmpty()) this.nameEn else "",
        poster = this.posterUrlPreview,
        rating = null,
    )
}

fun FilmPremier.convertToModelGenres(): List<GenresModel> {
    return this.genres.map { genre ->
        GenresModel(
            genre = genre.genre
        )
    }
}