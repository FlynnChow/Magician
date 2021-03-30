package com.flynnchow.zero.component.multi_recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class DataBindingViewHolder<Binding:ViewBinding>(val mBinding:Binding): RecyclerView.ViewHolder(mBinding.root)