package com.example.pengmob_jovan.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    @SerializedName("category_id")
    val categoryId: Int,
    val category: Category = Category(0, "", "", 0),
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val img: String
)
