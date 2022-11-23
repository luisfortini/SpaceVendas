package br.com.spaceinformatica.spacevendas.model

data class FilialModel(
    val filCodigo: Int,
    val filFantasia: String
){

    fun getFantasia(): String? {
        return filFantasia
    }
}