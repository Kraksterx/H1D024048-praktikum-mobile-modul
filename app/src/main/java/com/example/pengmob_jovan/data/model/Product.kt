package com.example.pengmob_jovan.data.model

data class Product(
    val id: Int,
    val categoryId: Int,
    val category: Category,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val img: String
)
