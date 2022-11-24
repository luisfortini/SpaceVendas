package br.com.spaceinformatica.spacevendas.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface EndPoint {

    @GET("apivendas/appfilial.rule?sys=AVR")
    fun getFiliais(@Query("usuario") usuario: String): Call<ResponseBody>

    @POST("vendasguardian/autenticacao/entrar")
    fun postLogin(@Body requestBody: RequestBody): Call<ResponseBody>

    @GET("apivendas/appcliente.rule?sys=AVR")
    fun getClientes(
        @Query("usuario") usuario: String,
        @Query("filial") filial: Int
    ): Call<ResponseBody>

    @GET("apivendas/appproduto.rule?sys=AVR")
    fun getProdutos(@Query("filial") filia: Int): Call<ResponseBody>

}