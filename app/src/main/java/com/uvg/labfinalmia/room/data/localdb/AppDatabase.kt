package com.uvg.labfinalmia.room.data.localdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.uvg.labfinalmia.room.data.localdb.dao.CryptoAssetDao
import com.uvg.labfinalmia.room.data.localdb.entity.CryptoAssetEntity

@Database(entities = [CryptoAssetEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cryptoAssetDao(): CryptoAssetDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nashebase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}