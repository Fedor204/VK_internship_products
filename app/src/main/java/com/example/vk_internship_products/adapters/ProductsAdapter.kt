package com.example.vk_internship_products.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vk_internship_products.R
import com.example.vk_internship_products.pojo.ProductInfoDto
import com.example.vk_internship_products.databinding.ProductItemBinding


class ProductsAdapter :
    RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    var productList: List<ProductInfoDto> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()

        }

    var onReachEndListener: OnReachEndListener? = null
        set(value) {
            field = value
        }

    var onProductClickListener: OnProductClickListener? = null
        set(value) {
            field = value
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(
            LayoutInflater.from(
                parent.context
            ),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun getItemCount() = productList.size


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        with(holder) {
            binding.tvTitle.text = product.title
            binding.tvDescription.text = product.description
            Glide.with(holder.itemView.context)
                .load(product.thumbnail)
                .into(binding.ivPosterRV)
            val rating = product.rating
            val backgroundId: Int
            if (rating > 4.5) {
                backgroundId = R.drawable.circle_green
            } else if (rating > 3.5) {
                backgroundId = R.drawable.circle_orange
            } else {
                backgroundId = R.drawable.circle_red
            }
            val background = ContextCompat.getDrawable(holder.itemView.context, backgroundId)
            binding.textViewRating.setBackground(background)
            binding.textViewRating.setText(
                String.format("%.1f", product.rating).toString()
            )

            if (position >= productList.size - 10 && onReachEndListener != null) {
                onReachEndListener?.onReachEnd()
            }

            itemView.setOnClickListener {
                onProductClickListener?.onProductClick(product)
            }
        }
    }

    inner class ProductViewHolder(val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnProductClickListener {
        fun onProductClick(products: ProductInfoDto)
    }

    interface OnReachEndListener {
        fun onReachEnd()
    }
}