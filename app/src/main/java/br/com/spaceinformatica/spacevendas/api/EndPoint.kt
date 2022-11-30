package br.com.spaceinformatica.spacevendas.api

import br.com.spaceinformatica.spacevendas.utils.TOKEN
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*


interface EndPoint {

    @GET("apivendas/appfilial.rule?sys=AVR")
    fun getFiliais(@Query("usuario") usuario: String): Call<ResponseBody>

    @POST("vendasguardian/autenticacao/entrar")
    fun postLogin(@Body requestBody: RequestBody): Call<ResponseBody>

    @GET("apivendas/appcliente.rule?sys=AVR")
    fun getClientes(
        @Query("usuario") usuario: String,
        @Query("filial") filial: Int,
    ): Call<ResponseBody>

    @GET("apivendas/appproduto.rule?sys=AVR")
    fun getProdutos(@Query("filial") filia: Int): Call<ResponseBody>

    @GET("apivendas/appunidadepro.rule?sys=AVR")
    fun getUnidadePro(
        @Query("filial") filial: Int,
        @Query("codigoproduto") codigoproduto: Int,
    ): Call<ResponseBody>

    @GET("apivendas/appdadosfecharpedido.rule?sys=AVR")
    fun getDadosFecharPedido(
        @Query("codigoCliente") codigoCliente: Int,
        @Query("usuario") usuario: String,
        @Query("filial") filial: Int
    ): Call<ResponseBody>


    @POST("vendasguardian/pedido")
    fun postSalvarPedido(@Body requestBody: RequestBody,
    @Header("Authorization") authorization: String
    ): Call<ResponseBody>
}