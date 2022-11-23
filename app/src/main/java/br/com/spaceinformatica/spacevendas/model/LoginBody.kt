package br.com.spaceinformatica.spacevendas.model

data class LoginBody(
    var login: String,
    var senha: String,
    var filialCodigo: Int
)
