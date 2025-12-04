package com.example.roomgps.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.roomgps.data.dao.GpsDao
import com.example.roomgps.data.dao.IbilbideaDao
import com.example.roomgps.data.dao.KotxeaDao
import com.example.roomgps.data.entity.Gps
import com.example.roomgps.data.entity.Ibilbidea
import com.example.roomgps.data.entity.Kotxea

@Database(
    entities = [Gps::class, Ibilbidea::class, Kotxea::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gpsDao(): GpsDao
    abstract fun ibilbideaDao(): IbilbideaDao
    abstract fun kotxeaDao(): KotxeaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gps_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val now = System.currentTimeMillis()
                            // Insertar varias coordenadas de ejemplo directamente con SQL
                            db.execSQL("INSERT INTO gps (latitude, longitude, destinoNombre, timestamp) VALUES (40.416775, -3.70379, 'Madrid - Sol', $now)")
                            db.execSQL("INSERT INTO gps (latitude, longitude, destinoNombre, timestamp) VALUES (41.387917, 2.169918, 'Barcelona - Sagrada Familia', $now)")
                            db.execSQL("INSERT INTO gps (latitude, longitude, destinoNombre, timestamp) VALUES (37.389092, -5.984459, 'Sevilla - Plaza de España', $now)")
                            db.execSQL("INSERT INTO gps (latitude, longitude, destinoNombre, timestamp) VALUES (43.263012, -2.934985, 'Bilbao - Guggenheim', $now)")
                            db.execSQL("INSERT INTO gps (latitude, longitude, destinoNombre, timestamp) VALUES (39.469907, -0.376288, 'Valencia - Ciudad de las Artes', $now)")
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
