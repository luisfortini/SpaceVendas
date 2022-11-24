package br.com.spaceinformatica.spacevendas.model

import com.google.gson.annotations.SerializedName

data class ProdutoModel(
    @SerializedName("Codigo")
    val codigoProduto: Int,

    @SerializedName("Produto")
    val descProduto: String,

    @SerializedName("Unidade")
    val unidadePro: String,

    @SerializedName("QtdeUnidade")
    val qtdeUnidade: Int,

    @SerializedName("PrecoVenda")
    val precoVenda: Double,

    @SerializedName("EstDisp")
    val estoqueDisp: Double,

    @SerializedName("CodBarras")
    val codBarras: String,

    @SerializedName("Referencia")
    val referencia: String
)
