package com.allever.app.translation.text.ui.adapter

import android.content.Context
import com.allever.app.translation.text.R
import com.allever.app.translation.text.bean.Lang
import com.allever.app.translation.text.bean.SelectLangItem
import com.allever.lib.common.ui.widget.recycler.BaseRecyclerViewAdapter
import com.allever.lib.common.ui.widget.recycler.BaseViewHolder

class SelectLangAdapter(context: Context, layoutResId: Int, data: MutableList<SelectLangItem>) :
    BaseRecyclerViewAdapter<SelectLangItem>(context, layoutResId, data) {
    override fun bindHolder(holder: BaseViewHolder, position: Int, item: SelectLangItem) {
        holder.setText(R.id.tvLanguage, item.lang?.KEY ?: Lang.CHINESE.KEY)
    }
}