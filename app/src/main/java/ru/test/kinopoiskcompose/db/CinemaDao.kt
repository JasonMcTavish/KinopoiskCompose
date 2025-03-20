package ru.test.kinopoiskcompose.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
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
import ru.test.kinopoiskcompose.db.model.FilmWithDetailInfo
import ru.test.kinopoiskcompose.db.model.FilmWithGenres
import ru.test.kinopoiskcompose.db.model.FilmsShortInfo
import ru.test.kinopoiskcompose.db.model.HistoryFilms
import ru.test.kinopoiskcompose.db.model.NewFilmCountries
import ru.test.kinopoiskcompose.db.model.NewFilmGenres
import ru.test.kinopoiskcompose.db.model.NewFilmImage
import ru.test.kinopoiskcompose.db.model.NewFilmInCollection
import ru.test.kinopoiskcompose.db.model.NewFilmPersons
import ru.test.kinopoiskcompose.db.model.NewFilmSimilar
import ru.test.kinopoiskcompose.db.model.NewFilmTopType
import ru.test.kinopoiskcompose.db.model.NewPersonFilms
import ru.test.kinopoiskcompose.db.model.NewSeasonEpisode
import ru.test.kinopoiskcompose.db.model.PersonFilms
import ru.test.kinopoiskcompose.db.model.PersonShortInfo
import ru.test.kinopoiskcompose.db.model.PersonWithDetailInfo
import ru.test.kinopoiskcompose.db.model.SeasonEpisode


@Dao
interface CinemaDao {
    // TABLE film_short_info
    @Insert(entity = FilmsShortInfo::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilmShortInfo(film: List<FilmsShortInfo>)

    @Insert(entity = FilmsShortInfo::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneFilmShortInfo(film: FilmsShortInfo)


    // TABLE film_top_types
    @Insert(entity = FilmTopType::class)
    suspend fun insertFilmTopTypes(films: List<NewFilmTopType>)

    @Query("DELETE FROM film_top_type WHERE film_id = :filmId AND category_name = :categoryName")
    suspend fun deleteFilmByCategory(filmId: Int, categoryName: String)

    @Query("DELETE FROM film_top_type WHERE category_name = :category")
    suspend fun clearFilmTopTypesByCategory(category: String)


    // TABLE film_markers
    @Insert(entity = FilmMarkers::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFilmMarkers(markers: FilmMarkers)

    @Query("UPDATE film_markers " +
            "SET is_favorite = :isFavorite, in_collection = :inCollection, is_viewed = :isViewed " +
            "WHERE id = :filmId")
    suspend fun updateFilmMarkers(
        filmId: Int,
        isFavorite: Int,
        inCollection: Int,
        isViewed: Int
    )

    @Query("UPDATE film_markers SET is_viewed = 0")
    suspend fun clearAllFromViewed()


    // TABLE collections
    @Insert(entity = CollectionFilms::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCollection(newCollection: CollectionFilms)

    @Query("DELETE FROM collections WHERE collection_name = :name")
    suspend fun deleteCollection(name: String)


    // TABLE film_in_collection
    @Insert(entity = FilmInCollection::class)
    suspend fun insertFilmInCollection(filmInCollection: NewFilmInCollection)

    @Query("DELETE FROM film_in_collection WHERE collection_name = :collectionName")
    suspend fun clearCollection(collectionName: String)

    @Query("DELETE FROM film_in_collection WHERE collection_name = :collectionName AND film_id = :filmId")
    suspend fun deleteFilmFromCollection(filmId: Int, collectionName: String)

    @Query("SELECT COUNT(*) FROM film_in_collection WHERE collection_name = :collectionName AND film_id = :filmId")
    fun checkFilmInCollection(collectionName: String, filmId: Int): Int

    @Query("SELECT COUNT(*) FROM film_in_collection WHERE collection_name = :collectionName")
    fun getCollectionSize(collectionName: String): Int

    @Query("UPDATE collections SET collection_size = :size WHERE collection_name = :name")
    suspend fun updateCollectionSize(name: String, size: Int)



    // TABLE films_history
    @Insert(entity = HistoryFilms::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistoryFilms(filmId: HistoryFilms)

    @Query("DELETE FROM films_history")
    suspend fun clearHistoryFilms()


    // TABLE films_detail_info
    @Insert(entity = FilmDetailInfo::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilmDetailInfo(filmInfo: FilmDetailInfo)

    @Query("DELETE FROM films_detail_info WHERE id = :filmId")
    suspend fun clearFilmDetailInfo(filmId: Int)


    // TABLE film_genres
    @Insert(entity = FilmGenres::class)
    suspend fun insertFilmGenres(genres: List<NewFilmGenres>)

    @Query("DELETE FROM film_genres")
    suspend fun clearFilmGenres()

    @Query("DELETE FROM film_genres WHERE film_id = :filmId")
    suspend fun deleteFilmGenres(filmId: Int)


    // TABLE film_countries
    @Insert(entity = FilmCountries::class)
    suspend fun insertFilmCountries(countries: List<NewFilmCountries>)

    @Query("DELETE FROM film_countries WHERE film_id = :filmId")
    suspend fun clearCountriesByFilm(filmId: Int)


    // TABLE films_gallery
    @Insert(entity = FilmImage::class)
    suspend fun insertFilmGallery(gallery: List<NewFilmImage>)

    @Query("DELETE FROM films_gallery WHERE film_id = :filmId")
    suspend fun clearGalleryByFilm(filmId: Int)


    // TABLE film_persons
    @Insert(entity = FilmPersons::class)
    suspend fun insertFilmPersons(persons: List<NewFilmPersons>)

    @Query("DELETE FROM film_persons WHERE film_id = :filmId")
    suspend fun cleaFilmPersons(filmId: Int)


    // TABLE person_short_info
    @Insert(entity = PersonShortInfo::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPersonShortInfo(person: List<PersonShortInfo>)

    @Insert(entity = PersonShortInfo::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOnePersonShortInfo(person: PersonShortInfo)


    // TABLE person_films
    @Insert(entity = PersonFilms::class)
    suspend fun insertPersonFilms(films: List<NewPersonFilms>)

    @Query("DELETE FROM person_films WHERE person_id = :personId")
    suspend fun clearPersonFilms(personId: Int)


    // TABLE film_similar
    @Insert(entity = FilmSimilar::class)
    suspend fun insertSimilar(similar: List<NewFilmSimilar>)

    @Query("DELETE FROM film_similar WHERE film_id = :filmId")
    suspend fun clearSimilar(filmId: Int)


    // TABLE seasons_episode
    @Insert(entity = SeasonEpisode::class)
    suspend fun insertSeasonEpisodes(seasons: List<NewSeasonEpisode>)

    @Query("DELETE FROM seasons_episode WHERE film_id = :filmId")
    suspend fun clearSeriesEpisodes(filmId: Int)


    // ---- GETTING DATA ----
    @Query("SELECT * FROM films_short_info WHERE id = :filmId")
    fun getFilmShortInfo(filmId: Int): FilmsShortInfo

    @Query("SELECT COUNT(*) FROM films_short_info WHERE id = :filmId")
    fun getFilmShortInfoCount(filmId: Int): Int

    @Transaction
    @Query("SELECT * FROM film_top_type " +
            "INNER JOIN films_short_info ON films_short_info.id = film_top_type.film_id " +
            "WHERE category_name = :categoryName")
    fun getFilmsByTopType(categoryName: String): List<FilmWithGenres>

    @Query("SELECT * FROM film_top_type " +
            "INNER JOIN films_short_info ON films_short_info.id = film_top_type.film_id " +
            "WHERE category_name = :categoryName ORDER BY rating ASC")
    fun getFilmByTopCategoryPaging(categoryName: String): PagingSource<Int, FilmWithGenres>

    @Query("SELECT * FROM films_short_info WHERE id = :filmId")
    fun getCurrentFilmShortInfo(filmId: Int): FilmWithGenres

    @Query("SELECT * FROM films_short_info WHERE id = :filmId")
    fun getCurrentFilmDetailInfo(filmId: Int): FilmWithDetailInfo

    @Query("SELECT * FROM person_short_info WHERE id = :personId")
    fun getPersonWithFilms(personId: Int): PersonWithDetailInfo

    @Query("SELECT * FROM film_persons WHERE film_id = :filmId")
    suspend fun getAllPersonsByFilm(filmId: Int): List<FilmPersons>

    @Query("SELECT * FROM person_films WHERE person_id = :personId")
    suspend fun getFilmsByPerson(personId: Int): List<PersonFilms>

    @Query("SELECT * FROM films_gallery WHERE film_id = :filmId")
    suspend fun getFilmGallery(filmId: Int): List<FilmImage>

    @Query("SELECT * FROM film_markers WHERE id = :filmId")
    fun getFilmMarkers(filmId: Int): Flow<FilmMarkers>

    @Query("SELECT * FROM film_markers " +
            "INNER JOIN films_short_info ON films_short_info.id = film_markers.id " +
            "WHERE is_viewed = 1")
    fun getAllViewedFilms(): Flow<List<FilmWithGenres>>

    @Query("SELECT COUNT(*) FROM film_markers WHERE is_favorite = 1")
    fun getCountFavoriteFilms(): Int

    @Query("SELECT * FROM film_markers " +
            "INNER JOIN films_short_info ON films_short_info.id = film_markers.id " +
            "WHERE is_favorite = 1")
    fun getAllFavoriteFilms(): Flow<List<FilmWithGenres>>

    @Query("SELECT * FROM films_history " +
            "INNER JOIN films_short_info ON films_short_info.id = films_history.id")
    fun getAllFilmsHistory(): Flow<List<FilmWithGenres>>

    @Query("SELECT * FROM seasons_episode WHERE film_id = :filmId")
    fun getSeriesSeasonsWithEpisodes(filmId: Int): List<SeasonEpisode>


    @Query("SELECT * FROM collections")
    fun getAllCollections(): Flow<List<CollectionFilms>>

    @Query("SELECT * FROM film_in_collection " +
            "INNER JOIN films_short_info ON films_short_info.id = film_in_collection.film_id " +
            "WHERE collection_name = :collectionName")
    fun getAllFilmInCollections(collectionName: String): List<FilmWithDetailInfo>
}