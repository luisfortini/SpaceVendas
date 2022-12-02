package br.com.spaceinformatica.spacevendas.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.spaceinformatica.spacevendas.R
import br.com.spaceinformatica.spacevendas.model.ItemMenu

class MenuAdapter(
    val context: Context,
    val itensMenu: List<ItemMenu>,
    val onItemClickListener: (Int) -> Unit,
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_menu_home, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(itensMenu[position])
    }

    override fun getItemCount(): Int {
        return itensMenu.size
    }


    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ItemMenu) {
            val imageItem = itemView.findViewById<ImageView>(R.id.item_menu_image)
            val labelItem = itemView.findViewById<TextView>(R.id.item_menu_label)
            val itemMenu = itemView.findViewById<LinearLayout>(R.id.item_menu)

            imageItem.setImageResource(item.imageID)
            labelItem.setText(item.label)
            itemMenu.setOnClickListener {
                onItemClickListener.invoke(item.id)
            }

        }

    }
}