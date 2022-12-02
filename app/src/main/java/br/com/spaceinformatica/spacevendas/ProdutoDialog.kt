package br.com.spaceinformatica.spacevendas

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment
import br.com.spaceinformatica.spacevendas.api.EndPoint
import br.com.spaceinformatica.spacevendas.api.HTTPClient
import br.com.spaceinformatica.spacevendas.model.ItensPedido
import br.com.spaceinformatica.spacevendas.model.UnidadeProModel
import br.com.spaceinformatica.spacevendas.utils.FILIAL
import br.com.spaceinformatica.spacevendas.utils.NUMERO_ITEM
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProdutoDialog(private val produtoCodigo: Int) : DialogFragment() {

    private lateinit var textDesc: TextView
    private lateinit var textEstoque: TextView
    private lateinit var inputQtde: EditText
    private lateinit var inputPreco: EditText
    private lateinit var spinnerUnidade: Spinner
    private lateinit var buttonSave: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.modal_produto, container)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textDesc = view.findViewById(R.id.desc_modal_produto)
        textEstoque = view.findViewById(R.id.estoque_modal)
        inputPreco = view.findViewById(R.id.input_preco_modal)
        inputPreco.setSelectAllOnFocus(true)
        inputQtde = view.findViewById(R.id.input_qtde_modal)
        inputQtde.setSelectAllOnFocus(true)

        buttonSave = view.findViewById(R.id.btn_save_produto)
        buttonSave.setOnClickListener {
            addProduto()

        }

        getUnidadePro()

    }

    private fun getUnidadePro() {
        HTTPClient.retrofit()
            .create(EndPoint::class.java)
            .getUnidadePro(FILIAL.toInt(), produtoCodigo)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    if (response.isSuccessful) {
                        val data = JSONObject(response.body()?.string()!!)
                        if (data.getBoolean("resposta")) {
                            setValuesUnidadePro(data)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun setValuesUnidadePro(data: JSONObject) {

        val unidadeProArray = data.getJSONArray("dados")
        val type = object : TypeToken<List<UnidadeProModel>>() {}.type
        val unidadeProList = Gson()
            .fromJson<List<UnidadeProModel>>(unidadeProArray.toString(), type)
        spinnerUnidade = view?.findViewById(R.id.spinner_modal_produto)!!
        val adapterUnidadePro = ArrayAdapter(view?.context!!,
            R.layout.support_simple_spinner_dropdown_item,
            unidadeProList)
        spinnerUnidade.adapter = adapterUnidadePro
        spinnerUnidade.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val precoVenda = unidadeProList[position].precoVenda
                val estoque = unidadeProList[position].estoqueDisp

                textEstoque.text = "Estoque: $estoque"
                inputPreco.setText(precoVenda.toString())

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        textDesc.text = "${unidadeProList[0].codigoProduto} - ${unidadeProList[0].descProduto}"
        textEstoque.text = "Estoque: ${unidadeProList[0].estoqueDisp}"
        inputPreco.setText(unidadeProList[0].precoVenda.toString())

    }

    private fun addProduto() {

        val unidadeProModel: UnidadeProModel =
            spinnerUnidade.adapter.getItem(spinnerUnidade.selectedItemPosition) as UnidadeProModel
        val codigoProduto = unidadeProModel.codigoProduto
        val descProduto = unidadeProModel.descProduto
        val unidade = unidadeProModel.unidadePro
        val qtdeUnidade = unidadeProModel.qtdeUnidade
        val fatVenda = unidadeProModel.fatorVenda
        val fatEstoque = unidadeProModel.fatorEstoque
        val precoTabela = unidadeProModel.precoVenda
        val quantidade = inputQtde.text.toString().toDouble()
        val precoVenda = inputPreco.text.toString().toDouble()
        val numeroItem = NUMERO_ITEM

        val itensPedido = ItensPedido(numeroItem,
            codigoProduto,
            descProduto,
            unidade,
            qtdeUnidade,
            fatEstoque,
            fatVenda,
            quantidade,
            precoVenda,
            precoTabela)

        Thread {
            val dao = (activity?.application as App).db.itensPedidoDao()
            dao.insertItens(itensPedido)
        }.start()
        NUMERO_ITEM = numeroItem + 1
        dismiss()

    }
}


