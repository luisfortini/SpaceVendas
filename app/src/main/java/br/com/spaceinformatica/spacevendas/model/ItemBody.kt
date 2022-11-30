package br.com.spaceinformatica.spacevendas.model

class ItemBody(
    val produtoCodigo: Int,
    val quantidade: Double,
    val unidade: String,
    val unidadeQuantidade: Int,
    val valorDesconto: Double = 0.0,
    val valorLiquido: Double,
    val valorUnitario: Double
)
