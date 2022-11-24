package br.com.spaceinformatica.spacevendas.api

import br.com.spaceinformatica.spacevendas.utils.getUrlBase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object HTTPClient {

    private fun httpCliente(): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(getUrlBase())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpCliente())
            .build()
    }

}