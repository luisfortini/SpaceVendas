package br.com.spaceinformatica.spacevendas.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.spaceinformatica.spacevendas.R
import br.com.spaceinformatica.spacevendas.model.ClienteModel

class ClienteAdapter(
    private val context: Context,
    private val clienteList: List<ClienteModel>,
    private val onItemClickListener: (Int) -> Unit)
    :RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val clienteLayoutItem =
            LayoutInflater.from(context).inflate(R.layout.cliente_item, parent, false)
        return ClienteViewHolder(clienteLayoutItem)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = clienteList[position]
        holder.setValues(cliente)

    }

    override fun getItemCount(): Int {
        return clienteList.size
    }


    inner class ClienteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun setValues(clienteModel: ClienteModel){
            val codigoCliente = itemView.findViewById<TextView>(R.id.cod_cliente)
            codigoCliente.text = "Código: ${clienteModel.codigoCliente}"
            val fantasiaCliente = itemView.findViewById<TextView>(R.id.primary_name)
            fantasiaCliente.text = clienteModel.fantasiaCliente
            val razaoCliente = itemView.findViewById<TextView>(R.id.secundary_name)
            razaoCliente.text = clienteModel.razaoCliente
            val limiteCliente = itemView.findViewById<TextView>(R.id.limite_credito)
            limiteCliente.text = "Limite: ${clienteModel.limiteCliente}"
            val cardCliente = itemView.findViewById<ConstraintLayout>(R.id.card_cliente)
            cardCliente.setOnClickListener {
                onItemClickListener.invoke(adapterPosition)
            }

        }

    }
}