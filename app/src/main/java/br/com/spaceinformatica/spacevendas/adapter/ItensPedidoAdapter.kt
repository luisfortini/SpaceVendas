package br.com.spaceinformatica.spacevendas.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.spaceinformatica.spacevendas.R
import br.com.spaceinformatica.spacevendas.model.ItensPedido

class ItensPedidoAdapter(
    val context: Context,
    val itenslist: List<ItensPedido>,
    val onItemClickListener:(Int) -> Unit,
): RecyclerView.Adapter<ItensPedidoAdapter.ItensPedidoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItensPedidoViewHolder {
        val itensPedidoLayout =
            LayoutInflater.from(context).inflate(R.layout.produto_itens_pedido, parent, false)
        return ItensPedidoViewHolder(itensPedidoLayout)
    }

    override fun onBindViewHolder(holder: ItensPedidoViewHolder, position: Int) {
        holder.setValues(itenslist[position])
    }

    override fun getItemCount(): Int {
        return itenslist.size
    }

    inner class ItensPedidoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun setValues(itensPedido: ItensPedido){

            val codigo = itemView.findViewById<TextView>(R.id.pro_codigo_item)
            codigo.text = "Código: ${itensPedido.codigoProduto}"

            val descricao = itemView.findViewById<TextView>(R.id.pro_desc_item)
            descricao.text = itensPedido.descProduto

            val unidade = itemView.findViewById<TextView>(R.id.unidade_item)
            unidade.text = "${itensPedido.unidade} / ${itensPedido.qtdeUnidade}"

            val qtde = itemView.findViewById<TextView>(R.id.qtde_item)
            qtde.text = "Qtde: ${itensPedido.quantidade}"

            val vrUnit = itemView.findViewById<TextView>(R.id.preco_unit)
            vrUnit.text = "Vr.Unit: R$ ${itensPedido.precoVenda}"

            val vrTotal = itemView.findViewById<TextView>(R.id.valor_total_item)
            vrTotal.text = "Vr.Total: R$ ${itensPedido.quantidade * itensPedido.precoVenda}"

            val cardItens = itemView.findViewById<ConstraintLayout>(R.id.card_itens_pedido)
            cardItens.setOnClickListener {
                onItemClickListener(itensPedido.numItem)
            }

        }
    }
}