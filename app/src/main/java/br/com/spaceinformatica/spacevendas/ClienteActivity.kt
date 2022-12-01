package br.com.spaceinformatica.spacevendas

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.spaceinformatica.spacevendas.adapter.ClienteAdapter
import br.com.spaceinformatica.spacevendas.api.EndPoint
import br.com.spaceinformatica.spacevendas.api.HTTPClient
import br.com.spaceinformatica.spacevendas.model.ClienteModel
import br.com.spaceinformatica.spacevendas.model.ItensPedido
import br.com.spaceinformatica.spacevendas.utils.*
import com.google.android.material.navigation.NavigationView
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ClienteActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var itensPedido: List<ItensPedido>
    private lateinit var clienteList: List<ClienteModel>
    private lateinit var clienteAdapter: ClienteAdapter
    private lateinit var searchCliente: EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente)

        progressBar = findViewById(R.id.progress_cliente)
        getClientes(FILIAL.toInt(), USUARIO)

    }

    private fun getClientes(filial: Int, usuario: String) {


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
                            clienteList = mutableListOf()
                            clienteList = GsonBuilder()
                                .create()
                                .fromJson(clienteArray.toString(), Array<ClienteModel>::class.java)
                                .toList()

                            setAdapter(clienteList)
                            getSeachCliente(clienteList)


                        } else {
                            Toast.makeText(
                                this@ClienteActivity, data.getString("dados"),
                                Toast.LENGTH_LONG
                            ).show()
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
        clienteAdapter = ClienteAdapter(this, clienteList) { id ->
            verificaPedidoEmDigitacao(clienteList[id])
        }

        val rv = findViewById<RecyclerView>(R.id.rv_clientes)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = clienteAdapter

    }

    private fun verificaPedidoEmDigitacao(cliente: ClienteModel) {
        Thread {
            itensPedido = getItensPedido(this@ClienteActivity)

            runOnUiThread {
                if (itensPedido.isEmpty()) {

                    CLIENTE_ATIVO = cliente
                    val intent = Intent(this, ProdutoActivity::class.java)
                    startActivity(intent)

                } else {

                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("Já existe um pedido em digitação.")
                    dialog.setMessage("Deseja cancelar pedido?")
                    dialog.setPositiveButton(android.R.string.ok) { dialog, which ->
                        Thread {
                            deleteItensPedido(this)
                        }.start()
                        NUMERO_ITEM = 1
                        CLIENTE_ATIVO = cliente
                        val intent = Intent(this, ProdutoActivity::class.java)
                        startActivity(intent)

                    }
                    dialog.create().show()

                }

            }
        }.start()

    }

    private fun getSeachCliente(listCliente: List<ClienteModel>) {
        searchCliente = findViewById(R.id.search_cliente)
        searchCliente.setSelectAllOnFocus(true)
        searchCliente.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val newListClient = mutableListOf<ClienteModel>()
                listCliente.forEach {
                    if (it.fantasiaCliente.contains(s.toString().uppercase()!!)
                        || it.razaoCliente.contains(s.toString().uppercase()!!)
                        || it.codigoCliente.toString().contains(s.toString().uppercase()!!)
                    ){
                    newListClient.add(it)
                }
                }
                newListClient.toList()
                setAdapter(newListClient)

                val textMsg = findViewById<TextView>(R.id.text_msg)

                if (newListClient.isEmpty()){
                    textMsg.text = "Nenhum cliente encontrado"
                } else {
                    textMsg.text = ""
                }

            }
        })

    }
}


