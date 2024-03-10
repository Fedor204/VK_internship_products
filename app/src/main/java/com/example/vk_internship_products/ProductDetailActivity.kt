package com.example.vk_internship_products

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.vk_internship_products.pojo.ProductInfoDto
import com.example.vk_internship_products.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {


    private val binding by lazy {
        ActivityProductDetailBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val product = intent.getParcelableExtra(EXTRA_PRODUCT_DETAIL, ProductInfoDto::class.java)

        binding.backButton.setOnClickListener {
            finish()
        }

        Glide.with(this)
            .load(product?.thumbnail)
            .into(binding.imageViewPoster)
        product?.let {
            with(binding) {
                tvTitleDetail.text = it.title
                tvDescriptionDetail.text = it.description
                tvBrandDetail.text = it.brand
                tvPriceDetail.text = String.format("Price: %.2f", it.price)
                tvStockDetail.text = String.format("In stock: %d", it.stock)
                tvDiscountDetail.text = String.format("Discount: %.0f%%", it.discountPercentage);
            }

        }

    }






    companion object {

        private const val EXTRA_PRODUCT_DETAIL = "extra_product_detail"

        fun newIntent(context: Context, product: ProductInfoDto): Intent {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra(EXTRA_PRODUCT_DETAIL, product)
            return intent
        }
    }
}