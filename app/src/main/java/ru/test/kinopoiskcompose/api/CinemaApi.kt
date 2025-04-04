package ru.test.kinopoiskcompose.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.test.kinopoiskcompose.entity.ResponseFilmDetailById
import ru.test.kinopoiskcompose.entity.ResponseFilmsByFilter
import ru.test.kinopoiskcompose.entity.ResponseFilmsTop
import ru.test.kinopoiskcompose.entity.ResponseGalleryByFilmId
import ru.test.kinopoiskcompose.entity.ResponseGenresCountries
import ru.test.kinopoiskcompose.entity.ResponsePersonById
import ru.test.kinopoiskcompose.entity.ResponsePersonByName
import ru.test.kinopoiskcompose.entity.ResponsePersonsByFilmId
import ru.test.kinopoiskcompose.entity.ResponsePremier
import ru.test.kinopoiskcompose.entity.ResponseSeasons
import ru.test.kinopoiskcompose.entity.ResponseSimilarFilmsByFilmId

interface CinemaApi {

    // FragmentHome
    @GET("v2.2/films/top")
    suspend fun getFilmsTop(
        @Query("type") type: String,
        @Query("page") page: Int
    ): ResponseFilmsTop

    @GET("v2.2/films/premieres")
    suspend fun getPremier(
        @Query("year") year: Int,
        @Query("month") month: String
    ): ResponsePremier

    @GET("v2.2/films")
    suspend fun getSerialsTop(
        @Query("type") type: String,
        @Query("page") page: Int
    ): ResponseFilmsByFilter

    // ----------------------------------

    // FragmentFilmDetail
    @GET("v2.2/films/{id}")
    suspend fun getFilmById(
        @Path("id") id: Int
    ): ResponseFilmDetailById

    @GET("v1/staff")
    suspend fun getPersons(
        @Query("filmId") filmId: Int
    ): List<ResponsePersonsByFilmId>

    @GET("v2.2/films/{id}/images")
    suspend fun getFilmImages(
        @Path("id") id: Int,
        @Query("type") type: String = "STILL",
        @Query("page") page: Int
    ): ResponseGalleryByFilmId

    // FragmentFilmDetail (series)
    @GET("v2.2/films/{id}/seasons")
    suspend fun getSeasons(
        @Path("id") id: Int
    ): ResponseSeasons

    @GET("v2.2/films/{id}/similars")
    suspend fun getSimilarFilms(
        @Path("id") id: Int
    ): ResponseSimilarFilmsByFilmId

    // FragmentPersonDetail
    @GET("v1/staff/{id}")
    suspend fun getPersonById(
        @Path("id") id: Int
    ): ResponsePersonById

    //  FragmentSearch
    @GET("v2.2/films")
    suspend fun getFilmsByFilter(
        @Query("countries") countries: String,
        @Query("genres") genres: String,
        @Query("order") order: String,
        @Query("type") type: String,
        @Query("ratingFrom") ratingFrom: Int,
        @Query("ratingTo") ratingTo: Int,
        @Query("yearFrom") yearFrom: Int,
        @Query("yearTo") yearTo: Int,
        @Query("keyword") keyword: String,
        @Query("page") page: Int
    ): ResponseFilmsByFilter

    @GET("v2.2/films/filters")
    suspend fun getGenresCountries(): ResponseGenresCountries

    // Fragment search (search person)
    @GET("v1/persons")
    suspend fun getPersonByName(
        @Query("name") name: String,
        @Query("page") page: Int
    ): ResponsePersonByName

    companion object {
        private const val BASE_URL = "https://kinopoiskapiunofficial.tech/api/"

        //        private const val API_KEY = "d466a9b0-1d29-4de2-bbb7-29db58da2787"
//        private const val API_KEY ="3923fc5d-a906-4cb5-8cbe-f40d9cc6e259"
//        private const val API_KEY = "00ec3c68-8c85-4bd5-8508-024db2f99a4c"
        private const val API_KEY = "f746dfa5-8093-401b-8df2-e84042f3dc96"


        private val interceptor = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(API_KEY))
            .build()

        fun getInstance(): CinemaApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(interceptor)
                .build()
                .create(CinemaApi::class.java)
        }
    }
}