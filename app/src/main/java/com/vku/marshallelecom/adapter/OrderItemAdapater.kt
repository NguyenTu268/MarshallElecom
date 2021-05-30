package com.vku.marshallelecom.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vku.marshallelecom.AppConfig
import com.vku.marshallelecom.R
import com.vku.marshallelecom.Tools
import com.vku.marshallelecom.model.Cart
import kotlinx.android.synthetic.main.order_item_row.view.*

class OrderItemAdapater(var items : ArrayList<Cart>, var context : Context) : RecyclerView.Adapter<OrderItemAdapater.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img = itemView.product_img
        var tvName = itemView.product_title
        var tvSize = itemView.order_size
        var tvAmount = itemView.order_num
        var tvPrice = itemView.item_price
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutinflater = LayoutInflater.from(parent.context)
        var view = layoutinflater.inflate(R.layout.order_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = items.get(position)
        holder.tvName.text = item.product.name
        holder.tvSize.text = item.detail.size
        holder.tvAmount.text = item.amount.toString()
        holder.tvPrice.text = Tools.format_currency(item.amount*item.detail.price)
        Glide.with(context)
                .load(AppConfig.IMAGE_URL+item.product.img)
                .centerCrop()
                .into(holder.img)
    }
}