package com.flynnchow.zero.magician.gallery.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flynnchow.zero.magician.R

val mColor = arrayOf(
    Color.RED,
    Color.GREEN,
    Color.BLUE,
    Color.YELLOW,
)

class TestAdapter:RecyclerView.Adapter<TestModel>() {
    var type = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestModel {
        val view = if (type == 0){
            LayoutInflater.from(parent.context).inflate(R.layout.item_test,parent,false)
        }else{
            LayoutInflater.from(parent.context).inflate(R.layout.item_test2,parent,false)
        }
        return TestModel(view)
    }

    override fun onBindViewHolder(holder: TestModel, position: Int) {
        holder.view.setBackgroundColor(mColor[position%4])
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }
}

class TestModel(val view: View):RecyclerView.ViewHolder(view){

}