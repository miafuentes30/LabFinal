package com.uvg.labfinalmia.room.data.localdb.di

import android.content.Context
import androidx.room.Room
import com.uvg.labfinalmia.room.data.localdb.AppDatabase


object Dependencies {
    private var database: AppDatabase? = null

    private fun buildDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "uvg.db"
        ).build()
    }

    fun provideDatabase(context: Context): AppDatabase {
        /*
            Este siguiente bloque de código es una versión mas éxplicita y sencilla de entender
            que la que no está comentada. La dejo aquí como referencia.
            if (database == null) {
                synchronized(this) {
                    if (database == null) {
                        database = buildDatabase(context)
                    }
                }
            }
            return database!!
         */

        /*
            Puntos importantes:
                synchronized(this) bloquea lo que esté dentro de su bloque para que ningún otro
                hilo, pueda accederlo mientras se esté usando.
                .also { ... } hace que, luego de crear buildDatabase(context), ejecuta lo que
                esté dentro del also, haciendo que "it" sea igual a lo que retorna ese método,
                en este caso, un objeto de tipo AppDatabase.
         */
        return database ?: synchronized(this) {
            database ?: buildDatabase(context).also { database = it }
        }
    }
}