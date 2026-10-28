package com.saluddocs.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DocumentoLocal::class],
    version = 1,
    exportSchema = false
)
abstract class SaludDocsDatabase : RoomDatabase() {

    abstract fun documentoLocalDao(): DocumentoLocalDao

    companion object {

        @Volatile
        private var INSTANCE: SaludDocsDatabase? = null

        fun obtenerBaseDeDatos(
            context: Context
        ): SaludDocsDatabase {

            return INSTANCE ?: synchronized(this) {

                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    SaludDocsDatabase::class.java,
                    "saluddocs_local.db"
                ).build()

                INSTANCE = instancia

                instancia
            }
        }
    }
}
