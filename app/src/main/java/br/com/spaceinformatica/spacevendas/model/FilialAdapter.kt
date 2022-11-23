package br.com.spaceinformatica.spacevendas.model

import android.R
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginTop


class FilialAdapter(val context: Context, val filiais: List<FilialModel>): BaseAdapter() {
//    override fun registerDataSetObserver(observer: DataSetObserver?) {
//        TODO("Not yet implemented")
//    }
//
//    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
//        TODO("Not yet implemented")
//    }

    override fun getCount(): Int {
        return filiais.size
    }

    override fun getItem(position: Int): Any {
        return filiais[position].filFantasia
    }

    override fun getItemId(position: Int): Long {
        return filiais[position].filCodigo.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val textView :TextView =  TextView(context)
        textView.setPadding(50,50,50,50)
        textView.setTextSize(15.toFloat())
        textView.setText(filiais[position].filFantasia)

        return textView

    }




}