package br.com.spaceinformatica.spacevendas.utils

import android.app.Activity
import br.com.spaceinformatica.spacevendas.App
import br.com.spaceinformatica.spacevendas.DadosPedidoFragment
import br.com.spaceinformatica.spacevendas.model.ClienteModel
import br.com.spaceinformatica.spacevendas.model.Config
import br.com.spaceinformatica.spacevendas.model.ItensPedido

lateinit var URL_BASE: String
lateinit var TOKEN: String
lateinit var FILIAL: String
var NUMERO_ITEM: Int = 1
lateinit var USUARIO: String
lateinit var CLIENTE_ATIVO :ClienteModel


fun getConfig(activity: Activity): List<Config> {

    lateinit var response: List<Config>
    val app = activity.application as App
    val dao = app.db.configDao()
    response = dao.getConfig()
    return response
}

fun createUrlBase(activity: Activity): String {
    val configList = getConfig(activity)
    if (configList.isNotEmpty()) {
        val host = configList[0].host
        val port = configList[0].porta
        val https = if (configList[0].https) {
            "https://"
        } else {
            "http://"
        }
        URL_BASE = "$https$host:$port/"
    }
    return URL_BASE
}

fun getUrlBase(): String {
    return URL_BASE
}

fun getItensPedido(activity: Activity):List<ItensPedido>{
    lateinit var response: List<ItensPedido>
    val app = activity.application as App
    val dao = app.db.itensPedidoDao()
    response = dao.getItensPedido()
    return response
}

fun deleteItensPedido(activity: Activity){
    val app = activity.application as App
    val dao = app.db.itensPedidoDao()
    dao.deleteItensPedido()
}

fun getBuscaTotalPedido(activity: Activity): Double{
    val app = activity.application as App
    val dao = app.db.itensPedidoDao()
    val response = dao.getTotalPedido()
    return response
}



