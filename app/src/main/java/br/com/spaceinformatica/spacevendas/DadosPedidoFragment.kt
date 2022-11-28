package br.com.spaceinformatica.spacevendas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import br.com.spaceinformatica.spacevendas.api.EndPoint
import br.com.spaceinformatica.spacevendas.api.HTTPClient
import br.com.spaceinformatica.spacevendas.model.CondicaoPagtoModel
import br.com.spaceinformatica.spacevendas.model.FormaPagtoModel
import br.com.spaceinformatica.spacevendas.model.NatOperModel
import br.com.spaceinformatica.spacevendas.model.VendedorModel
import br.com.spaceinformatica.spacevendas.utils.CLIENTE_ATIVO
import br.com.spaceinformatica.spacevendas.utils.FILIAL
import br.com.spaceinformatica.spacevendas.utils.USUARIO
import br.com.spaceinformatica.spacevendas.utils.getBuscaTotalPedido
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import br.com.spaceinformatica.spacevendas.DadosItensActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class DadosPedidoFragment : Fragment() {

    private lateinit var spinnerNatOper: Spinner
    private lateinit var spinnerFormaPagto: Spinner
    private lateinit var spinnerCondPagto: Spinner
    private lateinit var spinnerVendedor: Spinner
    private lateinit var textCliente: TextView
    private lateinit var textTotalPedido: TextView
    private lateinit var btnAddFloat: FloatingActionButton

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

        getDadosFecharPedido()

        textCliente = view.findViewById(R.id.desc_cliente)
        textCliente.text = "${CLIENTE_ATIVO.codigoCliente} - ${CLIENTE_ATIVO.fantasiaCliente}"

        getTotalPedido()

        btnAddFloat = view.findViewById(R.id.float_button_add_produto)
        btnAddFloat.setOnClickListener {
            activity?.finish()
        }

    }

    private fun getDadosFecharPedido() {

        HTTPClient.retrofit()
            .create(EndPoint::class.java)
            .getDadosFecharPedido(CLIENTE_ATIVO.codigoCliente, USUARIO, FILIAL.toInt())
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

    fun getTotalPedido() {
        Thread {
            val totalPedido = getBuscaTotalPedido(activity)

            kotlin.run {
                textTotalPedido = view?.findViewById(R.id.total_pedido)!!
                textTotalPedido.text = "Total do Pedido: R$ $totalPedido"
            }
        }.start()
    }

}