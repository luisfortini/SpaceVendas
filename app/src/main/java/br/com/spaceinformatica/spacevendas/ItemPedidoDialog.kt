package br.com.spaceinformatica.spacevendas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.DialogFragment
import br.com.spaceinformatica.spacevendas.utils.atualizaItemPedido
import br.com.spaceinformatica.spacevendas.utils.getItemPedido

class ItemPedidoDialog(val numItem: Int) : DialogFragment() {

    private lateinit var textDesc: TextView
    private lateinit var inputUnidade: EditText
    private lateinit var inputQtde: EditText
    private lateinit var inputPreco: EditText

    private lateinit var buttonSave: Button

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.modal_item_pedido, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Thread {
            val itemPedido = getItemPedido(activity, numItem)

            kotlin.run {
                textDesc = view.findViewById(R.id.desc_modal_item_pedido)
                textDesc.text = "${itemPedido.codigoProduto} - ${itemPedido.descProduto}"

                inputUnidade = view.findViewById(R.id.input_unidade_item_pedido)
                inputUnidade.setText("${itemPedido.unidade} / ${itemPedido.qtdeUnidade}")

                inputQtde = view.findViewById(R.id.input_qtde_modal_item_pedido)
                inputQtde.setSelectAllOnFocus(true)
                inputQtde.setText(itemPedido.quantidade.toString())

                inputPreco = view.findViewById(R.id.input_preco_modal_item_pedido)
                inputPreco.setSelectAllOnFocus(true)
                inputPreco.setText(itemPedido.precoVenda.toString())
            }
        }.start()

        buttonSave = view.findViewById(R.id.btn_save_produto_item_pedido)
        buttonSave.setOnClickListener {
            saveItemPedidoChange()
        }

    }

    private fun saveItemPedidoChange() {

        val qtde = inputQtde.text.toString().toDouble()
        val precoVenda = inputPreco.text.toString().toDouble()
        Thread {
            atualizaItemPedido(activity, numItem, qtde, precoVenda)
        }.start()

        Toast.makeText(view?.context!!, "Alteração gravada com sucesso!", Toast.LENGTH_SHORT)
        dismiss()
    }
}
