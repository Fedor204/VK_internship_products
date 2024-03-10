package com.example.vk_internship_products

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vk_internship_products.api.ApiFactory
import com.example.vk_internship_products.pojo.ProductInfoDto
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var currentPage = 0
    private val pageSize = 20

    private val _products = MutableLiveData<List<ProductInfoDto>>()
    val products: LiveData<List<ProductInfoDto>>
        get() = _products

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>>
        get() = _categories

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<Boolean>()
    val errorMessage: LiveData<Boolean>
        get() = _errorMessage


    private val compositeDisposable = CompositeDisposable()



    init {
        loadProducts("All categories")
    }

    fun loadCategories() {

        val disposable = ApiFactory.apiService.loadProductsCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ categories ->
                val allCategories = mutableListOf("All categories")
                allCategories.addAll(categories)
                _categories.value = allCategories
            }, { error ->
                Log.e("MainViewModel", "Error loading categories: $error")
            })
        compositeDisposable.add(disposable)
    }

    fun loadProducts(category: String = "All categories", skip : Int = currentPage * pageSize) {
        _errorMessage.value = false
        val loading = isLoading.value
        if (loading != null && loading) {
            return
        }



        val apiCall = if (category == "All categories") {

            ApiFactory.apiService.loadProducts(skip)

        } else {
            ApiFactory.apiService.loadProductsByCategory(category)
        }


        val disposable = apiCall
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _isLoading.value = true
            }
            .doAfterTerminate {
                _isLoading.value = false
            }
            .subscribe( {
                val newProducts = it.products
                if (newProducts.isEmpty()) {
                    //end of products
                } else {
                    if (category == "All categories") {
                        val currentProducts = _products.value.orEmpty().toMutableList()
                        currentProducts.addAll(newProducts)
                        _products.value = currentProducts
                        currentPage++
                    } else {
                        _products.value = newProducts
                        currentPage = 0
                    }
                }
            }, {

                _errorMessage.value = true
        })
        compositeDisposable.add(disposable)
    }


    fun clearProducts() {
        _products.value = emptyList()
    }



    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}