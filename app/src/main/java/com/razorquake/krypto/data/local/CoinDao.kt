package com.razorquake.krypto.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.razorquake.krypto.data.remote.dto.Coin
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {
    @Query("SELECT * FROM Coin")
    fun getCoins(): Flow<List<Coin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoin(coin: Coin)

    @Update
    suspend fun updateCoin(coin: Coin)

    @Query("DELETE FROM Coin WHERE id = :id")
    suspend fun deleteCoin(id: String)


    @Query("SELECT * FROM Coin WHERE id = :id")
    suspend fun getCoin(id: String): Coin?
}