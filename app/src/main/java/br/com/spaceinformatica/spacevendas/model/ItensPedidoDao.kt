package br.com.spaceinformatica.spacevendas.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItensPedidoDao {

    @Insert
    fun insertItens(itensPedido: ItensPedido)

    @Query("SELECT * FROM ITENSPEDIDO")
    fun getItensPedido(): List<ItensPedido>

    @Query("DELETE FROM ITENSPEDIDO")
    fun deleteItensPedido()

    @Query("DELETE FROM ITENSPEDIDO WHERE NUMITEM = :numItem")
    fun deleteItem(numItem: Int)

    @Query("SELECT * FROM ITENSPEDIDO WHERE NUMITEM = :numItem")
    fun getItemPedido(numItem: Int): List<ItensPedido>

    @Query("SELECT SUM(QUANTIDADE * PRECOVENDA)  TOTAL FROM ITENSPEDIDO")
    fun getTotalPedido():Double

    @Query("UPDATE ITENSPEDIDO SET QUANTIDADE = :qtde, PRECOVENDA = :precoVenda WHERE NUMITEM = :numItem")
    fun atualizaItemPedido(numItem: Int, qtde: Double, precoVenda: Double)
}