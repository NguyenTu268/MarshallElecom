package com.vku.marshallelecom.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vku.marshallelecom.R
import com.vku.marshallelecom.adapter.CategoryAdapter
import com.vku.marshallelecom.viewmodel.ShopViewModel
import com.vku.marshallelecom.model.Category
import kotlinx.android.synthetic.main.fragment_shop.*
import kotlinx.android.synthetic.main.fragment_shop.view.*


class ShopFragment : Fragment() {
    var scrollPositionX : Int = 0
    var scrollPositionY : Int = 0
    private lateinit var rootView : View
    private lateinit var shopViewModel : ShopViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        shopViewModel.getCategoriesLiveData().observe(this, Observer {
            var categoryAdapter = CategoryAdapter(it, this.context!!)
            rootView.rv_category.adapter = categoryAdapter
        })
        var banners = ArrayList<Int>()
        banners.add(R.drawable.banner1)
        banners.add(R.drawable.banner2)
        banners.add(R.drawable.banner3)
        banners.add(R.drawable.banner4)

        rootView = inflater.inflate(R.layout.fragment_shop, container, false)
        
        rootView.carouselView.pageCount = banners.size
        rootView.carouselView.setImageListener { position, imageView ->
            imageView.setImageResource(banners.get(position));
        }

        rootView.rv_category.setHasFixedSize(true)
        rootView.rv_category.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        return rootView
    }
    override fun onPause() {
        super.onPause()
        // Save ListView state @ onPause
        scrollPositionX = rootView.scrollX
        scrollPositionY = rootView.scrollY
        Log.d("TAG", "saving fragment state = $scrollPositionX / $scrollPositionY")
    }

    override fun onResume() {
        super.onResume()
        rootViewScroll.smoothScrollTo(scrollPositionX,scrollPositionY)
        Log.d("TAG", "paste fragment state = $scrollPositionX / $scrollPositionY")
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ShopFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}