package br.com.spaceinformatica.spacevendas.model

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
//@Database(
//    entities = [Config::class],
//    version = 1,
//)

@Database(
    entities = [Config::class, ItensPedido::class],
    version = 3,
    autoMigrations = [AutoMigration(from = 2, to = 3)]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun configDao(): ConfigDao
    abstract fun itensPedidoDao(): ItensPedidoDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "vendasdb"
                    ).build()
                }
                INSTANCE as AppDatabase
            } else {
                INSTANCE as AppDatabase
            }


        }
    }

}
