package br.com.spaceinformatica.spacevendas.model

import com.google.gson.annotations.SerializedName

data class FilialModel(
    @SerializedName("Filial")
    val filCodigo: Int,
    @SerializedName("Fantasia")
    val filFantasia: String
){
    override fun toString(): String {
        return filCodigo.toString() + " - " + filFantasia
    }
}
