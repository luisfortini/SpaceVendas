package br.com.spaceinformatica.spacevendas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.spaceinformatica.spacevendas.adapter.ProdutoAdapter
import br.com.spaceinformatica.spacevendas.api.EndPoint
import br.com.spaceinformatica.spacevendas.api.HTTPClient
import br.com.spaceinformatica.spacevendas.model.ProdutoModel
import br.com.spaceinformatica.spacevendas.utils.FILIAL
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class ProdutoActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produto)

        progressBar = findViewById(R.id.progress_produto)
        getProdutos()
    }

    private fun getProdutos() {

        HTTPClient.retrofit()
            .create(EndPoint::class.java)
            .getProdutos(FILIAL.toInt())
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val data = JSONObject(response.body()?.string())
                        if (data.getBoolean("resposta")) {
                            val produtoArray = data.getJSONArray("dados")
                            val produtoList = GsonBuilder()
                                .create()
                                .fromJson(produtoArray.toString(), Array<ProdutoModel>::class.java)
                                .toList()

                            setAdapterProduto(produtoList)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@ProdutoActivity,
                        "Falha na conexão! Verifique a internet ou as configurações!",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }

    private fun setAdapterProduto(produtoList: List<ProdutoModel>) {
        val produtoAdapter = ProdutoAdapter(this, produtoList) { id ->
            val dialog = ProdutoDialog(id)
            dialog.show(supportFragmentManager, dialog.tag)
        }

        val rv = findViewById<RecyclerView>(R.id.rv_produtos)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = produtoAdapter
    }
}