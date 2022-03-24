package com.stark.sqldelightapp.di

import android.app.Application
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.stark.sqldelightapp.PersonDatabase
import com.stark.sqldelightapp.data.PersonDataSource
import com.stark.sqldelightapp.data.PersonDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideSqlDriver(application: Application): SqlDriver {
        return AndroidSqliteDriver(
            schema = PersonDatabase.Schema,
            context = application,
            name = "person.db"
        )
    }

    @Provides
    @Singleton
    fun provideDataSource(sqlDriver: SqlDriver): PersonDataSource {
        return PersonDataSourceImpl(PersonDatabase(sqlDriver))
    }

}