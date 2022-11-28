package br.com.spaceinformatica.spacevendas.model

import com.google.gson.annotations.SerializedName

data class CondicaoPagtoModel(
    @SerializedName("codigo")
    val codigoCondicao: Int,

    @SerializedName("descricao")
    val condicaoDesc: String,

    @SerializedName("prazo")
    val prazoCondicao: Int
){
    override fun toString(): String {
        return condicaoDesc
    }
}
