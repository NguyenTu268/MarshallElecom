package com.vku.marshallelecom.model

import com.google.gson.annotations.SerializedName

class ProductDetail() {
    @SerializedName("detail_id")
    var id = 0
    var product_id = 0

    @SerializedName("product_size")
    var size = ""

    @SerializedName("product_amount")
    var amount = 0

    @SerializedName("product_price")
    var price = 0
}