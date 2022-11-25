package br.com.spaceinformatica.spacevendas


import android.os.Bundle
import android.view.*
import android.widget.*
import android.widget.AdapterView.*
import androidx.fragment.app.DialogFragment
import br.com.spaceinformatica.spacevendas.api.EndPoint
import br.com.spaceinformatica.spacevendas.api.HTTPClient
import br.com.spaceinformatica.spacevendas.model.UnidadeProModel
import br.com.spaceinformatica.spacevendas.utils.FILIAL
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProdutoDialog(val produtoCodigo: Int) : DialogFragment() {

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

        getUniadePro()

    }

    fun getUniadePro() {
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

        textDesc = view?.findViewById(R.id.desc_modal_produto)!!
        textEstoque = view?.findViewById(R.id.estoque_modal)!!
        inputPreco = view?.findViewById(R.id.input_preco_modal)!!
        inputPreco.setSelectAllOnFocus(true)
        inputQtde = view?.findViewById(R.id.input_qtde_modal)!!
        inputQtde.setSelectAllOnFocus(true)

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

}


