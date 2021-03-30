package com.flynnchow.zero.component.multi_recyclerview

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class IMultiAdapter:RecyclerView.Adapter<IMultiHolder<*>>() {
    private val mRegister = SparseArray<IMultiCreator>()
    protected val mData = ArrayList<IMultiData<*>>()
    private val _extra = ArrayList<IMultiData<*>>()

    override fun getItemCount(): Int {
        return mData.size + _extra.size
    }

    fun getDataCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IMultiHolder<*> {
        return mRegister[viewType].create(parent.context,parent)
    }

    override fun onBindViewHolder(holder: IMultiHolder<*>, position: Int) {
        holder.bindData(getItemData(position)!!,position)
    }

    override fun getItemViewType(position: Int): Int {
        if (position < mData.size){
            return mData[position].getType()
        }else{
            return _extra[position - mData.size].getType()
        }
    }

    fun getItemData(position: Int): IMultiData<*>?{
        if (position >= itemCount){
            return null
        }
        else if (position < mData.size){
            return mData[position]
        }else{
            return _extra[position - mData.size]
        }
    }

    fun resetData(args:List<IMultiData<*>>, anim:Boolean = true){
        if (mData.size > 0)
            mData.clear()
        addData(args,anim)
    }

    fun addData(arg: IMultiData<*>, position :Int  = mData.size, anim:Boolean = true){
        val mPosition = when {
            position >= mData.size -> {
                mData.size
            }
            position <= 0 -> {
                0
            }
            else -> {
                position
            }
        }
        if (anim){
            mData.add(mPosition,arg)
            notifyItemInserted(mPosition)
        }else{
            mData.add(mPosition,arg)
            notifyDataSetChanged()
        }
    }

    fun addData(args: List<IMultiData<*>>, anim:Boolean = true){
        if (anim){
            val start = mData.size
            mData.addAll(args)
            notifyItemRangeChanged(start,args.size)
            notifyDataSetChanged()
        }else{
            mData.addAll(args)
            notifyDataSetChanged()
        }
    }

    fun removeData(arg: IMultiData<*>):Boolean{
        val result = mData.remove(arg)
        if (result) notifyDataSetChanged()
        return result
    }

    fun changeData(position:Int, arg: IMultiData<*>, anim: Boolean = true){
        if (position < itemCount){
            mData[position] = arg
            notifyItemChanged(position)
        }
    }

    fun removeData(position:Int,anim: Boolean = true):Boolean{
        if (position < 0 || position >= mData.size){
            return false
        }
        if (anim){
            mData.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,mData.size + _extra.size - position)
        }else{
            removeData(mData[position])
        }
        return true
    }

    fun registryCreator(type:Int,creator: IMultiCreator){
        mRegister.put(type,creator)
    }

    fun clearData(){
        mData.clear()
        notifyDataSetChanged()
    }

    fun clearData(start:Int,end:Int = mData.size){
        for (index in end-1 downTo start){
            mData.removeAt(index)
        }
        notifyDataSetChanged()
    }

    open fun changeData(data:Any){
        //子类重写
    }
}