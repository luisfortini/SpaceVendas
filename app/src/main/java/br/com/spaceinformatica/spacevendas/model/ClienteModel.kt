package br.com.spaceinformatica.spacevendas.model

import com.google.gson.annotations.SerializedName

data class ClienteModel(
    @SerializedName("Codigo")
    val codigoCliente: Int,

    @SerializedName("Fantasia")
    val fantasiaCliente: String,

    @SerializedName("Razao")
    val razaoCliente: String,

    @SerializedName("LimiteCred")
    val limiteCliente: Double
)
