package br.com.spaceinformatica.spacevendas.model

import com.google.gson.annotations.SerializedName

data class NatOperModel(
    @SerializedName("Nat_Codigo")
    val codigoNatureza: String,

    @SerializedName("Nat_Desc")
    val naturezaDesc: String,
){
    override fun toString(): String {
        return "$codigoNatureza - $naturezaDesc"
    }
}
