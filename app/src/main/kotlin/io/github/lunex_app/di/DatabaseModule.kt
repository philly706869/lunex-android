package io.github.lunex_app.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.lunex_app.data.local.LunexDao
import io.github.lunex_app.data.local.LunexDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideLunexDatabase(@ApplicationContext context: Context): LunexDatabase {
        return Room.databaseBuilder(context, LunexDatabase::class.java, "lunex").build()
    }

    @Provides
    fun provideLunexDao(database: LunexDatabase): LunexDao = database.lunexDao()
}
