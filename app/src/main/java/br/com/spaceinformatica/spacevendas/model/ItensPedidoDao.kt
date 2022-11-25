package br.com.spaceinformatica.spacevendas.model

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface ItensPedidoDao {

    @Insert
    fun insertItens(itensPedido: ItensPedido)
}