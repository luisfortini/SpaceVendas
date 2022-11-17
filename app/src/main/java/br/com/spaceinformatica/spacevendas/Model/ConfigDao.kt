package br.com.spaceinformatica.spacevendas.Model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ConfigDao {

    @Insert
    fun insertConfig(config: Config)

    @Query("SELECT * FROM CONFIG WHERE ID=1")
    fun getConfig(): List<Config>

    @Update
    fun updateConfig(config: Config)

}