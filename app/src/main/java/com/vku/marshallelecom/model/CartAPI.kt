package com.vku.marshallelecom.model

import com.google.gson.annotations.SerializedName

class CartAPI() {
    var carts = ArrayList<Cart>()

    @SerializedName("cost")
    var totalCost = 0
}