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
import br.com.spaceinformatica.spacevendas.model.ProdutoModel

class ProdutoAdapter(
    val context: Context,
    val produtoList: List<ProdutoModel>,
    val onItemClickListener: (Int) -> Unit,
) : RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ProdutoAdapter.ProdutoViewHolder {
        val produtoLayout =
            LayoutInflater.from(context).inflate(R.layout.produto_item, parent, false)
        return ProdutoViewHolder(produtoLayout)
    }

    override fun onBindViewHolder(holder: ProdutoAdapter.ProdutoViewHolder, position: Int) {
        holder.setValues(produtoList[position])
    }

    override fun getItemCount(): Int {
        return produtoList.size
    }

    inner class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setValues(produtoModel: ProdutoModel) {

            val codigo = itemView.findViewById<TextView>(R.id.pro_codigo)
            codigo.text = "Código: ${produtoModel.codigoProduto}"
            val descricao = itemView.findViewById<TextView>(R.id.pro_desc)
            descricao.text = produtoModel.descProduto
            val unidade = itemView.findViewById<TextView>(R.id.unidade)
            unidade.text = "${produtoModel.unidadePro} / ${produtoModel.qtdeUnidade}"
            val estoque = itemView.findViewById<TextView>(R.id.estoque)
            estoque.text = "Estoque: ${produtoModel.estoqueDisp}"
            val codBarras = itemView.findViewById<TextView>(R.id.cod_barras)
            codBarras.text = produtoModel.codBarras
            val preco = itemView.findViewById<TextView>(R.id.preco)
            preco.text = ("R$ ${produtoModel.precoVenda}")
            val cardProduto = itemView.findViewById<ConstraintLayout>(R.id.card_produto)
            cardProduto.setOnClickListener {
                onItemClickListener(produtoModel.codigoProduto)

            }
        }
    }
}