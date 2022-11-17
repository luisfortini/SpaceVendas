package br.com.spaceinformatica.spacevendas.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Config(
    @PrimaryKey(autoGenerate = false) val id: Int = 1,
    @ColumnInfo(name="host") val host: String = "",
    @ColumnInfo(name="port") val porta: Int = 0,
    @ColumnInfo(name="https") val https: Boolean = false
)
