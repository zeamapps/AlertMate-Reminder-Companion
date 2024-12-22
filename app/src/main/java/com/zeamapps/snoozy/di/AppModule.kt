package com.zeamapps.snoozy.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zeamapps.snoozy.data.LocalUserImpl
import com.zeamapps.snoozy.data.db.ReminderDao
import com.zeamapps.snoozy.data.db.ReminderDatabase
import com.zeamapps.snoozy.data.repository.ReminderRepo
import com.zeamapps.snoozy.domain.AppEntryUseCases
import com.zeamapps.snoozy.domain.LocalUserManager
import com.zeamapps.snoozy.domain.ReadAppEntry
import com.zeamapps.snoozy.domain.SaveAppEntry
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideLocalUserManager(application: Application): LocalUserManager =
        LocalUserImpl(application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(localUserManager: LocalUserManager) = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager)
    )

    @Provides
    @Singleton
    fun provideDatabase(application: Application): ReminderDatabase {
        return Room.databaseBuilder(application, ReminderDatabase::class.java, "wishlist.db")
            .build()
    }
    @Provides
    @Singleton
    fun provideReminderDao(database: ReminderDatabase) = database.reminderDao()

    @Provides
    @Singleton
    fun provideReminderRepo(reminderDao: ReminderDao) = ReminderRepo(reminderDao)


}