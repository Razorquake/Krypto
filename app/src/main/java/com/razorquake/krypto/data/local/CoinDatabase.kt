package com.razorquake.krypto.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.razorquake.krypto.data.remote.dto.Coin

@Database(entities = [Coin::class], version = 2)
@TypeConverters(CoinTypeConvertor::class)
abstract class CoinDatabase: RoomDatabase() {
    abstract fun coinDao(): CoinDao
    companion object {
        @Volatile
        private var INSTANCE: CoinDatabase? = null
        fun getInstance(context: Context): CoinDatabase{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }
        private fun buildDatabase(context: Context): CoinDatabase = Room.databaseBuilder(
            context.applicationContext,
            CoinDatabase::class.java,
            "coin_database"
        ).allowMainThreadQueries()
            .addTypeConverter(CoinTypeConvertor())
            .fallbackToDestructiveMigration()
            .build()
    }
}