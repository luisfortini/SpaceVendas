package br.com.spaceinformatica.spacevendas.model

import com.google.gson.annotations.SerializedName

class UnidadeProModel (

    @SerializedName("pro_codigo")
    val codigoProduto: Int,

    @SerializedName("pro_desc")
    val descProduto: String,

    @SerializedName("unp_unidade")
    val unidadePro: String,

    @SerializedName("unp_quantidade")
    val qtdeUnidade: Int,

    @SerializedName("unp_fatestoque")
    val fatorEstoque: Double,

    @SerializedName("unp_fatvenda")
    val fatorVenda: Double,

    @SerializedName("PrecoVenda")
    val precoVenda: Double,

    @SerializedName("EstDisp")
    val estoqueDisp: Double,
){

    override fun toString(): String {
        return "$unidadePro / $qtdeUnidade"
    }
}