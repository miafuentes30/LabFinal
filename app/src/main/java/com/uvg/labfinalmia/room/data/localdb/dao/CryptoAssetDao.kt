package com.uvg.labfinalmia.room.data.localdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uvg.labfinalmia.room.data.localdb.entity.CryptoAssetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoAssetDao {
    @Query("SELECT * FROM crypto_assets")
    suspend fun getAllCryptoAssets(): List<CryptoAssetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCryptoAssets(cryptoAssets: List<CryptoAssetEntity>)

    @Query("SELECT * FROM crypto_assets WHERE id = :id")
    suspend fun getCryptoAssetById(id: String): CryptoAssetEntity?

    @Query("DELETE FROM crypto_assets")
    suspend fun deleteAllCryptoAssets()
}