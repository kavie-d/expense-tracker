package com.example.expensetracker.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expensetracker.dao.EventDao
import com.example.expensetracker.dao.ExpensesDao
import com.example.expensetracker.entities.Event
import com.example.expensetracker.entities.Expense

@Database(entities = [Event::class, Expense::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseDataConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun eventDao(): EventDao
    abstract fun expenseDao(): ExpensesDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "expense_tracker_db")
                    .build()
                    .also { Instance = it }
            }
        }

    }

}