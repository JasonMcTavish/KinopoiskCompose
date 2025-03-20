package ru.test.kinopoiskcompose.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.test.kinopoiskcompose.db.model.FilmsShortInfo
import ru.test.kinopoiskcompose.db.model.NewFilmGenres
import ru.test.kinopoiskcompose.model.FilmShortInfoModel
import ru.test.kinopoiskcompose.model.GenresModel

@JsonClass(generateAdapter = true)
data class ResponseFilmsByFilter(
    @Json(name = "total") val total: Int,
    @Json(name = "totalPages") val totalPages: Int,
    @Json(name = "items") val films: List<FilmByFilter>
)

@JsonClass(generateAdapter = true)
data class FilmByFilter(
    @Json(name = "kinopoiskId") val filmId: Int,
    @Json(name = "imdbId") val imdbId: String?,
    @Json(name = "nameRu") val nameRu: String?,
    @Json(name = "nameEn") val nameEn: String?,
    @Json(name = "nameOriginal") val nameOriginal: String?,
    @Json(name = "posterUrlPreview") val posterPreview: String,
    @Json(name = "posterUrl") val posterUrl: String,
    @Json(name = "countries") val countries: List<Country>,
    @Json(name = "genres") val genres: List<Genre>,
    @Json(name = "ratingKinopoisk") val ratingKinopoisk: Double?,
    @Json(name = "ratingImdb") val ratingImdb: Double?,
    @Json(name = "type") val type: String,
    @Json(name = "year") val year: String?
)

fun FilmByFilter.convertForDbShortInfo(): FilmsShortInfo {
    return FilmsShortInfo(
        filmId = this.filmId,
        name = nameRu ?: nameEn?.toString() ?: nameOriginal ?: "",
        poster = this.posterPreview,
        rating = (ratingKinopoisk ?: ratingImdb).toString()
    )
}

fun FilmByFilter.convertForDbGenres(): List<NewFilmGenres> {
    return this.genres.map { genre ->
        NewFilmGenres(
            filmId = this.filmId,
            genre = genre.genre
        )
    }

}

fun FilmByFilter.convertToModelShortInfo(): FilmShortInfoModel {
    return FilmShortInfoModel(
        filmId = this.filmId,
        name = nameRu ?: nameEn?.toString() ?: nameOriginal ?: "",
        poster = this.posterPreview,
        rating = (ratingKinopoisk ?: ratingImdb).toString()
    )
}

fun FilmByFilter.convertToModelGenres(): List<GenresModel> {
    return this.genres.map { genre ->
        GenresModel(
            genre = genre.genre
        )
    }
}