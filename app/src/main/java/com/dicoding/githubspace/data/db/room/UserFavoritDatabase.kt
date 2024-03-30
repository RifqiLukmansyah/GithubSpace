package com.dicoding.githubspace.data.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.githubspace.data.db.dao.UserFavoritDao
import com.dicoding.githubspace.data.response.Users

@Database(entities = [Users::class], version = 2)
abstract class UserFavoritDatabase : RoomDatabase() {
    abstract fun UserFavoritDao(): UserFavoritDao

    companion object {
        @Volatile
        private var INSTANCE: UserFavoritDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserFavoritDatabase {
            if (INSTANCE == null) {
                synchronized(UserFavoritDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserFavoritDatabase::class.java, "fav_database"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE as UserFavoritDatabase
        }
    }
}