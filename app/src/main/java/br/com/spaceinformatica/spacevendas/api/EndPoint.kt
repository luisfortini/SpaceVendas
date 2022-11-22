package br.com.spaceinformatica.spacevendas.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EndPoint {

    @GET("apivendas/appfilial.rule?sys=AVR")
    fun getFiliais(@Query("usuario") usuario: String): Call<ResponseBody>
}