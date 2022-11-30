package br.com.spaceinformatica.spacevendas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.spaceinformatica.spacevendas.adapter.ProdutoAdapter
import br.com.spaceinformatica.spacevendas.api.EndPoint
import br.com.spaceinformatica.spacevendas.api.HTTPClient
import br.com.spaceinformatica.spacevendas.model.ClienteModel
import br.com.spaceinformatica.spacevendas.model.ProdutoModel
import br.com.spaceinformatica.spacevendas.utils.FILIAL
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class ProdutoActivity : AppCompatActivity() {

    private lateinit var buttonFloat: FloatingActionButton
    private lateinit var edit_Search: EditText
    private lateinit var produtoAdapter: ProdutoAdapter

    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produto)

        buttonFloat = findViewById(R.id.float_button_produtos)
        buttonFloat.setOnClickListener {
            startActivity(Intent(this,DadosItensActivity::class.java))
            finish()
        }

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
                            getSearchProduto(produtoList)
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
         produtoAdapter = ProdutoAdapter(this, produtoList) { id ->
            val dialog = ProdutoDialog(id)
            dialog.show(supportFragmentManager, dialog.tag)
        }

        val rv = findViewById<RecyclerView>(R.id.rv_produtos)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = produtoAdapter
    }

    private fun getSearchProduto(listProduto: List<ProdutoModel>){
        edit_Search = findViewById(R.id.edit_search_produto)
        edit_Search.setSelectAllOnFocus(true)
        edit_Search.addTextChangedListener (object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val newListProduto = mutableListOf<ProdutoModel>()

                listProduto.forEach {
                    if(it.descProduto.contains(s.toString().uppercase()!!)
                        ||it.codigoProduto.toString().contains(s!!)
                        ||it.codBarras.contains(s!!)){
                        newListProduto.add(it)
                    }

                }
                newListProduto.toList()
                setAdapterProduto(newListProduto)

                val textMsg = findViewById<TextView>(R.id.text_msg_produto)

                if (newListProduto.isEmpty()){
                    textMsg.text = "Nenhum produto encontrado"
                } else {
                    textMsg.text = ""
                }
            }
        })
    }
}