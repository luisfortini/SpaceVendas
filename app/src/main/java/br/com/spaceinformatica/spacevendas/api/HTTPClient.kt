package br.com.spaceinformatica.spacevendas.api

import android.telephony.mbms.StreamingServiceInfo
import br.com.spaceinformatica.spacevendas.App
import br.com.spaceinformatica.spacevendas.model.Config
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HTTPClient {

    private const val URL_BASE = "http://192.168.0.13:2020/"

    private fun httpCliente(): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpCliente())
            .build()
    }

}