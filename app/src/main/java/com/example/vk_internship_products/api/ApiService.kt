package com.example.vk_internship_products.api

import com.example.vk_internship_products.pojo.ProductListDto
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("products")
    fun loadProducts(@Query(QUERY_PARAM_SKIP) skip: Int, @Query(QUERY_PARAM_LIMIT) limit: Int = 20) : Single<ProductListDto>
    @GET("/products/category/{category}")
    fun loadProductsByCategory(@Path("category") category: String): Single<ProductListDto>

    @GET("/products/categories")
    fun loadProductsCategories(): Observable<List<String>>


    companion object {
        private const val QUERY_PARAM_LIMIT = "limit"
        private const val QUERY_PARAM_SKIP = "skip"

    }
}