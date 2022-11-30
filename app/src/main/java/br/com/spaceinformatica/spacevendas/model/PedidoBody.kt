package br.com.spaceinformatica.spacevendas.model

import java.text.SimpleDateFormat

class PedidoBody(
    val clienteCodigo: Int,
    val condicaoPagamentoCodigo: Int,
    val dataEmissao: String,
    val formaPagamentoCodigo: String,
    val horaEmissao: String,
    val naturezaOperacao: String,
    val numeroOrigem: String,
    val observacao: String,
    val observacaoFiscal1: String = "",
    val observacaoFiscal2: String = "",
    val observacaoFiscal3: String = "",
    val tipoEntrega: String = "BALCAO",
    val valorDesconto: Double = 0.0,
    val valorFrete: Double = 0.0,
    val valorLiquido: Double,
    val vendedorCodigo: Int,
    val items: List<ItemBody>
    )
