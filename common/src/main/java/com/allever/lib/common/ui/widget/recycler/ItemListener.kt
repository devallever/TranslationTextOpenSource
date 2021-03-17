package com.allever.lib.common.ui.widget.recycler

interface ItemListener {
    fun onItemClick(position: Int, holder: BaseViewHolder)
    fun onItemLongClick(position: Int, holder: BaseViewHolder): Boolean = false
}