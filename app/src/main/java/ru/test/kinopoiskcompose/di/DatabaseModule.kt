package ru.test.kinopoiskcompose.di

import android.content.Context
import androidx.room.Room
import ru.test.kinopoiskcompose.db.CinemaDao
import ru.test.kinopoiskcompose.db.CinemaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideCinemaDatabase(@ApplicationContext context: Context): CinemaDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CinemaDatabase::class.java,
            "Cinema.db"
        ).build()
    }

    @Provides
    fun provideCinemaDao(database: CinemaDatabase): CinemaDao {
        return database.cinemaDao()
    }
}