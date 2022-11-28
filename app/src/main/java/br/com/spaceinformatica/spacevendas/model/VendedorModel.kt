package br.com.spaceinformatica.spacevendas.model

import com.google.gson.annotations.SerializedName

data class VendedorModel(
    @SerializedName("CLB_CODIGO")
    val codigoVendedor: Int,
    @SerializedName("CLB_RAZAO")
    val descVendedor: String,
){
    override fun toString(): String {
        return "$codigoVendedor - $descVendedor"
    }
}
