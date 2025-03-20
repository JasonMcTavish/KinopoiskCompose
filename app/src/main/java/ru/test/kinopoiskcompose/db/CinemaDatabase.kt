package ru.test.kinopoiskcompose.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.test.kinopoiskcompose.db.model.CollectionFilms
import ru.test.kinopoiskcompose.db.model.FilmCountries
import ru.test.kinopoiskcompose.db.model.FilmDetailInfo
import ru.test.kinopoiskcompose.db.model.FilmGenres
import ru.test.kinopoiskcompose.db.model.FilmImage
import ru.test.kinopoiskcompose.db.model.FilmInCollection
import ru.test.kinopoiskcompose.db.model.FilmMarkers
import ru.test.kinopoiskcompose.db.model.FilmPersons
import ru.test.kinopoiskcompose.db.model.FilmSimilar
import ru.test.kinopoiskcompose.db.model.FilmTopType
import ru.test.kinopoiskcompose.db.model.FilmsShortInfo
import ru.test.kinopoiskcompose.db.model.HistoryFilms
import ru.test.kinopoiskcompose.db.model.PersonFilms
import ru.test.kinopoiskcompose.db.model.PersonShortInfo
import ru.test.kinopoiskcompose.db.model.SeasonEpisode


@Database(
    entities = [
        FilmsShortInfo::class,
        FilmDetailInfo::class,
        FilmPersons::class,
        FilmImage::class,
        FilmGenres::class,
        FilmCountries::class,
        PersonShortInfo::class,
        PersonFilms::class,
        CollectionFilms::class,
        FilmInCollection::class,
        FilmSimilar::class,
        SeasonEpisode::class,
        FilmMarkers::class,
        HistoryFilms::class,
        FilmTopType::class
    ],
    version = 1
)
abstract class CinemaDatabase : RoomDatabase() {
    abstract fun cinemaDao(): CinemaDao
}