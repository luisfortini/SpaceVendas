package br.com.spaceinformatica.spacevendas.model

import com.google.gson.annotations.SerializedName

data class FormaPagtoModel(
   @SerializedName("codigo")
   val codigoFormaPagto : String,

   @SerializedName("descricao")
   val formaPagtoDesc: String,
){
   override fun toString(): String {
      return "$codigoFormaPagto - $formaPagtoDesc"
   }
}