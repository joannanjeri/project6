package com.example.project6

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * abstract methods that define the DAOs that work with the database
 */

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    /**
     * @return returns the DAO for accessing the note table
     */
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        /**
         * this makes sure the database is using the singleton pattern
         *
         * @param context the context used to get the app level context for the database builder
         * @return the singleton instance of the app database
         */

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notes_db"
                ).build().also { instance = it }
            }
        }
    }
}