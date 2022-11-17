package br.com.spaceinformatica.spacevendas

import android.app.Application
import br.com.spaceinformatica.spacevendas.Model.AppDatabase

class App : Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {

        super.onCreate()
        db = AppDatabase.getDatabase(this)
    }

}