package com.example.vk_internship_products


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vk_internship_products.adapters.ProductsAdapter
import com.example.vk_internship_products.adapters.SpinnerCategoriesAdapter
import com.example.vk_internship_products.pojo.ProductInfoDto
import com.example.vk_internship_products.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var selectedCategory: String = ALL_CATEGORIES

    private lateinit var viewModel: MainViewModel

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val adapter = ProductsAdapter()
        binding.rwProducts.adapter = adapter
        binding.rwProducts.layoutManager = GridLayoutManager(this, 2)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.products.observe(this) {
            adapter.productList = it
        }
        viewModel.loadCategories()

        viewModel.categories.observe(this) { categories ->
            if (categories.isNotEmpty()) {
                val categoryAdapter =
                    SpinnerCategoriesAdapter(this, R.layout.spinner_item_layout, categories)
                categoryAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = categoryAdapter

            }
        }


        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCategory = binding.spinner.getItemAtPosition(position) as String
                viewModel.loadProducts(selectedCategory)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        binding.buttonRetry.setOnClickListener {
            viewModel.clearProducts()
            viewModel.loadProducts(selectedCategory)
        }

        viewModel.errorMessage.observe(this) {
            if (it) {
                with(binding) {
                    tvErrorMsg.visibility = View.VISIBLE
                    buttonRetry.visibility = View.VISIBLE
                    errorIcon.visibility = View.VISIBLE
                    if (binding.rwProducts.visibility == View.VISIBLE) {
                        binding.rwProducts.visibility = View.GONE
                    }
                }
            } else {
                with(binding) {
                    tvErrorMsg.visibility = View.GONE
                    buttonRetry.visibility = View.GONE
                    errorIcon.visibility = View.GONE
                    rwProducts.visibility = View.VISIBLE
                }
            }
        }


        viewModel.isLoading.observe(this) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        adapter.onReachEndListener = (object : ProductsAdapter.OnReachEndListener {
            override fun onReachEnd() {
                viewModel.loadProducts(selectedCategory)
            }
        })

        adapter.onProductClickListener = (object : ProductsAdapter.OnProductClickListener {

            override fun onProductClick(products: ProductInfoDto) {
                val intent = ProductDetailActivity.newIntent(this@MainActivity, products)
                startActivity(intent)
            }

        })
    }

    companion object {
        private const val ALL_CATEGORIES = "All categories"
    }
}