package br.com.spaceinformatica.spacevendas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.spaceinformatica.spacevendas.adapter.ClienteAdapter
import br.com.spaceinformatica.spacevendas.api.EndPoint
import br.com.spaceinformatica.spacevendas.api.HTTPClient
import br.com.spaceinformatica.spacevendas.model.ClienteModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClienteActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente)

        progressBar = findViewById(R.id.progress_cliente)

        val filial = intent?.getIntExtra("filial", 1)
        val usuario = intent?.getStringExtra("usuario")

        getClientes(filial!!, usuario!!)

    }

    fun getClientes(filial: Int, usuario: String) {


        HTTPClient.retrofit()
            .create(EndPoint::class.java)
            .getClientes(usuario, filial)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    if (response.isSuccessful) {

                        progressBar.visibility = View.GONE
                        val data = JSONObject(response.body()?.string()!!)
                        if (data.getBoolean("resposta")) {

                            val clienteArray = data.getJSONArray("dados")
                            val clienteList = GsonBuilder()
                                .create()
                                .fromJson(clienteArray.toString(), Array<ClienteModel>::class.java)
                                .toList()

                            setAdapter(clienteList)


                        } else {
                            Toast.makeText(
                                this@ClienteActivity, data.getString("dados"),
                                Toast.LENGTH_LONG
                            )
                            Log.i("teste", data.getString("dados"))
                        }

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@ClienteActivity,
                        "Falha na conexão! Verifique a internet ou as configurações!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            )

    }

    private fun setAdapter(clienteList: List<ClienteModel>) {
        val clienteAdapter = ClienteAdapter(this, clienteList ){ id ->
            Toast.makeText(this@ClienteActivity,id.toString(),Toast.LENGTH_SHORT).show()
        }

        val rv = findViewById<RecyclerView>(R.id.rv_clientes)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = clienteAdapter
    }
}