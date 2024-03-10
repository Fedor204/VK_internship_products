package com.example.vk_internship_products.pojo

import com.google.gson.annotations.SerializedName

data class ProductListDto(
    @SerializedName("products") val products: List<ProductInfoDto>
)
