package br.com.spaceinformatica.spacevendas

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.spaceinformatica.spacevendas.adapter.ItensPedidoAdapter
import br.com.spaceinformatica.spacevendas.model.ItensPedido
import br.com.spaceinformatica.spacevendas.utils.deleteItem
import br.com.spaceinformatica.spacevendas.utils.getItensPedido


class ItensPedidoFragment : Fragment() {

    lateinit var progressBar: ProgressBar

    companion object {
        fun newInstance() = ItensPedidoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_itens_pedido, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getItensPedido()

    }

    private fun setAdapterItensPedido(itensList: List<ItensPedido>) {
        val itensAdapter = ItensPedidoAdapter(view?.context!!, itensList) { id ->

            val dialog = onCreateDialog(null, id)
            dialog.show()

        }
        val rv = view?.findViewById<RecyclerView>(R.id.rv_itens_pedido)
        rv?.layoutManager = LinearLayoutManager(view?.context!!)
        rv?.adapter = itensAdapter


        progressBar = view?.findViewById(R.id.progress_itens_pedido)!!
        progressBar.visibility = View.GONE

    }

    private fun onCreateDialog(savedInstanceState: Bundle?, id: Int): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("O que deseja fazer?")
                .setItems(R.array.arrays_item_options,
                    DialogInterface.OnClickListener { dialog, which ->
                        if (which == 0) {
                            val dialog = ItemPedidoDialog(id)
                            dialog.show(childFragmentManager, dialog.tag)

                        } else if (which == 1) {
                            deletarItem(id)
                        }
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun deletarItem(numItem: Int) {

        val dialog = AlertDialog.Builder(view?.context!!)
        dialog.setTitle("Excluir item?")
        dialog.setMessage("Deseja realmente excluir este item?")
        dialog.setPositiveButton(android.R.string.ok) { dialog, which ->
            Thread {
                deleteItem(activity, numItem)
            }.start()
        }
        dialog.setNegativeButton(android.R.string.cancel) { dialog, which ->
            dialog.dismiss()
        }
        dialog.create().show()

    }

    private fun getItensPedido() {
        Thread {
            val listItensPedido = getItensPedido(activity!!)
            kotlin.run {
                setAdapterItensPedido(listItensPedido)

                val textMsg = view?.findViewById<TextView>(R.id.text_msg_itens_pedido)
                if (listItensPedido.isNotEmpty()) {
                    textMsg?.text = ""
                } else {
                    textMsg?.text = "Nenhum produto adicionado ao pedido."
                }
            }
        }.start()
    }

}

