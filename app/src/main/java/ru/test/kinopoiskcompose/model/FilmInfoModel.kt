package ru.test.kinopoiskcompose.model

data class FilmShortInfoModel(
    val filmId: Int,
    val name: String,
    val poster: String,
    val rating: String?
)

data class GenresModel(val genre: String)

data class FilmWithGenresModel(
    val film: FilmShortInfoModel,
    val genres: List<GenresModel>
)

data class FilmWithDetailInfoModel(
    val film: FilmShortInfoModel,
    val detailInfo: FilmDetailInfoModel,
    val genres: List<GenresModel>,
    val countries: List<FilmCountriesModel>,
    val persons: List<FilmPersonsModel>,
    val similar: List<FilmSimilarModel>,
    val gallery: List<FilmImageModel>,
    val seriesEpisodes: List<SeasonEpisodeModel>?
)


data class FilmDetailInfoModel(
    val filmId: Int,
    val year: Int?,
    val length: Int?,
    val shortDescription: String?,
    val description: String?,
    val type: String,
    val ageLimit: String?,
    val startYear: Int?,
    val endYear: Int?,
    val serial: Int?
)

data class FilmCountriesModel(val country: String)

data class FilmPersonsModel(
    val personId: Int,
    val professionKey: String,
    val description: String?,
    val name: String,
    val poster: String,
    val profession: String?
)

data class FilmSimilarModel(
    val filmId: Int,
    val name: String?,
    val poster: String,
    val posterPreview: String
)

data class FilmImageModel(
    val filmId: Int,
    val image: String,
    val preview: String,
    val imageCategory: String
)

data class SeasonEpisodeModel(
    val filmId: Int,
    val seriesNumber: Int,
    val episodeNumber: Int,
    val name: String?,
    val date: String?,
    val synopsis: String?
)