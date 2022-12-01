package br.com.spaceinformatica.spacevendas

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import br.com.spaceinformatica.spacevendas.api.EndPoint
import br.com.spaceinformatica.spacevendas.api.HTTPClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import br.com.spaceinformatica.spacevendas.model.*
import br.com.spaceinformatica.spacevendas.utils.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.*


class DadosPedidoFragment : Fragment() {

    private lateinit var spinnerNatOper: Spinner
    private lateinit var spinnerFormaPagto: Spinner
    private lateinit var spinnerCondPagto: Spinner
    private lateinit var spinnerVendedor: Spinner
    private lateinit var btnSavePedido: Button
    private lateinit var progressBar: ProgressBar

    companion object {
        fun newInstance() = DadosPedidoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_dados_pedido, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view?.findViewById(R.id.progress_dados_pedido_frag)!!
        progressBar.visibility = View.GONE

        //Chamada para atualizar a lista de produtos em Utils
        Thread {
            getItensPedido(activity)
        }.start()

        getDadosFecharPedido()

        btnSavePedido = view.findViewById(R.id.btn_save_pedido)
        btnSavePedido.setOnClickListener {
            savePedido()
        }

    }

    private fun getDadosFecharPedido() {

        HTTPClient.retrofit()
            .create(EndPoint::class.java)
            .getDadosFecharPedido(CLIENTE_ATIVO?.codigoCliente ?: 1, USUARIO, FILIAL.toInt())
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    if (response.isSuccessful) {
                        val data = JSONObject(response.body()?.string())
                        val natOperArray = data.getJSONArray("dadosNatureza")
                        val condPagtoArray = data.getJSONArray("dadosCondicao")
                        val formaPagtoArray = data.getJSONArray("dadosForma")
                        val vendedorArray = data.getJSONArray("dadosVendedor")

                        setSpinnerNatOper(natOperArray)
                        setSpinnerFormaPagto(formaPagtoArray)
                        setSpinnerCondPagto(condPagtoArray)
                        setSpinnerVendedor(vendedorArray)

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

    }

    fun setSpinnerNatOper(natOperArray: JSONArray) {
        val type = object : TypeToken<List<NatOperModel>>() {}.type
        val natOperList =
            Gson().fromJson<List<NatOperModel>>(natOperArray.toString(),
                type)
        val adapterNatOper = ArrayAdapter(view?.context!!,
            R.layout.support_simple_spinner_dropdown_item,
            natOperList)
        spinnerNatOper = view?.findViewById(R.id.spinner_natoper)!!
        spinnerNatOper.adapter = adapterNatOper
    }

    fun setSpinnerFormaPagto(formaPagtoArray: JSONArray) {
        val type = object : TypeToken<List<FormaPagtoModel>>() {}.type
        val list =
            Gson().fromJson<List<NatOperModel>>(formaPagtoArray.toString(),
                type)
        val adapter = ArrayAdapter(view?.context!!,
            R.layout.support_simple_spinner_dropdown_item,
            list)
        spinnerFormaPagto = view?.findViewById(R.id.spinner_forma_pagto)!!
        spinnerFormaPagto.adapter = adapter
    }

    fun setSpinnerCondPagto(condPagtoArray: JSONArray) {
        val type = object : TypeToken<List<CondicaoPagtoModel>>() {}.type
        val list =
            Gson().fromJson<List<NatOperModel>>(condPagtoArray.toString(),
                type)
        val adapter = ArrayAdapter(view?.context!!,
            R.layout.support_simple_spinner_dropdown_item,
            list)
        spinnerCondPagto = view?.findViewById(R.id.spinner_cond_pagto)!!
        spinnerCondPagto.adapter = adapter
    }

    fun setSpinnerVendedor(vendedorArray: JSONArray) {
        val type = object : TypeToken<List<VendedorModel>>() {}.type
        val list =
            Gson().fromJson<List<NatOperModel>>(vendedorArray.toString(),
                type)
        val adapter = ArrayAdapter(view?.context!!,
            R.layout.support_simple_spinner_dropdown_item,
            list)
        spinnerVendedor = view?.findViewById(R.id.spinner_vendedor)!!
        spinnerVendedor.adapter = adapter
    }

    fun savePedido() {


        progressBar.visibility = View.VISIBLE


        val itemsBody = createItemsBody()
        val pedidoBody = createPedidoBody(itemsBody)

        val bodyRequest = Gson().toJson(pedidoBody).toRequestBody("application/json".toMediaType())

        HTTPClient.retrofit()
            .create(EndPoint::class.java)
            .postSalvarPedido(bodyRequest, TOKEN)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val data = JSONObject(response.body()?.string()!!)
                        if(data.getInt("status")==200) {

                            val dialog = AlertDialog.Builder(view?.context!!)
                            dialog.setTitle("SUCESSO")
                            dialog.setMessage("Pedido gravado com sucesso!")
                            dialog.create().show()

                            resetPedido()
                        } else {

                            Toast.makeText(view?.context,data.getString("mensagemUsuario"),
                            Toast.LENGTH_LONG).show()
                        }

                    } else {
                        val dataError = JSONObject(response.errorBody()?.string()!!)
                        Toast.makeText(view?.context,
                            dataError.getString("mensagemUsuario"),
                            Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressBar.visibility = View.GONE

                    Toast.makeText(
                        view?.context,
                        "Falha na conexão! Verifique a internet ou as configurações!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })


    }

    fun createItemsBody(): List<ItemBody> {

        val listItems: MutableList<ItemBody> = mutableListOf()
        ITEMS_PEDIDO.forEach {
            listItems.add(ItemBody(
                it.codigoProduto,
                it.quantidade,
                it.unidade,
                it.qtdeUnidade,
                0.0,
                it.quantidade * it.precoVenda,
                it.precoVenda))
        }
        Log.i("teste", listItems.toString())
        return listItems
    }

    fun createPedidoBody(itemsBoby: List<ItemBody>): PedidoBody {

        val spinnerNatOper = view?.findViewById<Spinner>(R.id.spinner_natoper)
        val natOperModel: NatOperModel =
            spinnerNatOper?.adapter?.getItem(spinnerNatOper.selectedItemPosition) as NatOperModel

        val spinnerFormaPagto = view?.findViewById<Spinner>(R.id.spinner_forma_pagto)
        val formaPagtoModel: FormaPagtoModel =
            spinnerFormaPagto?.adapter?.getItem(spinnerFormaPagto.selectedItemPosition) as FormaPagtoModel

        val spinnerCondPagto = view?.findViewById<Spinner>(R.id.spinner_cond_pagto)
        val condPagtoModel: CondicaoPagtoModel =
            spinnerCondPagto?.adapter?.getItem(spinnerCondPagto.selectedItemPosition) as CondicaoPagtoModel

        val spinnerVendedor = view?.findViewById<Spinner>(R.id.spinner_vendedor)
        val vendedorModel: VendedorModel =
            spinnerVendedor?.adapter?.getItem(spinnerVendedor.selectedItemPosition) as VendedorModel

        val editObservacao = view?.findViewById<EditText>(R.id.input_obs)
        val observacao = editObservacao?.text.toString()

        val data = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()).toString()
        val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()).toString()
        val pedidoBody = PedidoBody(
            CLIENTE_ATIVO?.codigoCliente!!,
            condPagtoModel.codigoCondicao,
            data,
            formaPagtoModel.codigoFormaPagto,
            hora,
            natOperModel.codigoNatureza,
            "${vendedorModel.codigoVendedor}${data.replace("/", "")}${hora.replace(":","")} ",
            observacao,
            "",
            "",
            "",
            "BALCAO",
            0.0,
            0.0,
            TOTAL_PEDIDO,
            vendedorModel.codigoVendedor,
            itemsBoby
        )
        return pedidoBody
    }

    fun resetPedido(){
        NUMERO_ITEM = 1
        CLIENTE_ATIVO = null
        ITEMS_PEDIDO = mutableListOf()
        TOTAL_PEDIDO = 0.00

        Thread{
            deleteItensPedido(activity)
        }

        Toast.makeText(view?.context,
            "Pedido Gravado com Sucesso",
            Toast.LENGTH_LONG).show()

        activity?.finish()
    }
}


