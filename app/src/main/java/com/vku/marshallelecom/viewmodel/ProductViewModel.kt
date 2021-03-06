package com.vku.retrofitapicherrybridal.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vku.marshallelecom.AppConfig
import com.vku.marshallelecom.client.ProductClient
import com.vku.marshallelecom.model.Product
import com.vku.marshallelecom.model.ProductAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel : ViewModel() {

    var currentPage = 1
    var lastPage = Integer.MAX_VALUE

    private val productClient: ProductClient = AppConfig.retrofit.create(ProductClient::class.java)

    private var mProductsLiveData = MutableLiveData<ArrayList<Product>>()
    private var mProducts = ArrayList<Product>()

    private var mProductLiveData = MutableLiveData<Product>()
    private var mProduct = Product()

    fun getProductsLiveData() : MutableLiveData<ArrayList<Product>> {
        return this.mProductsLiveData
    }
    fun getProductLiveData() : MutableLiveData<Product> {
        return this.mProductLiveData
    }
    var isLoading = false
    fun getProduct(product_id : Int) {
        val productService = productClient.getProduct(product_id)
        productService.enqueue(object : Callback<Product>{
            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.d("ResponseProduct", "Fail: "+t.message)
            }

            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                val productResponse = response.body()
                Log.d("ResponseProduct", ""+productResponse)
                if(response.isSuccessful && productResponse!=null) {
                    mProductLiveData.value = productResponse!!
                }
            }

        })
    }
    fun getProducts(options : HashMap<String, String>) {
        isLoading = true
        options.remove("page")
        val productService = productClient.getProducts(options)
        productService.enqueue(object : Callback<ProductAPI> {
            override fun onFailure(call: Call<ProductAPI>, t: Throwable) {
                Log.d("ResponseProduct", "Fail: "+t.message)
                isLoading = false
            }
            override fun onResponse(call: Call<ProductAPI>, response: Response<ProductAPI>) {
                val productResponse = response.body()
                Log.d("ResponseProduct", ""+productResponse)
                if(response.isSuccessful && productResponse!=null) {
                    mProductsLiveData.value = productResponse.products
                    mProducts = productResponse.products
                    currentPage = productResponse.current_page
                    lastPage = productResponse.last_page
                    Log.d("LoadMore", "SIZE OLD ${mProducts.size}")
                    isLoading = false
                }
            }

        })
    }
    fun loadMore(options: HashMap<String, String>) {
        currentPage+=1
        if(currentPage<=lastPage) {
            isLoading = true
            options.put("page", "$currentPage")
            val productService = productClient.getProducts(options)
            productService.enqueue(object : Callback<ProductAPI> {
                override fun onFailure(call: Call<ProductAPI>, t: Throwable) {
                    Log.d("LoadMore", "Fail: "+t.message)
                    isLoading = false
                }
                override fun onResponse(call: Call<ProductAPI>, response: Response<ProductAPI>) {
//                    Handler(Looper.getMainLooper()).postDelayed({

                    val productResponse = response.body()
                    Log.d("LoadMore", ""+productResponse?.current_page)
                    if(response.isSuccessful && productResponse!=null) {
                        mProducts.addAll(productResponse.products)
                        mProductsLiveData.value = mProducts
                        Log.d("LoadMore", "SIZE NEW ${mProducts.size}")
                        isLoading = false
                    }
//                    }, 1000)
                }

            })
        }
    }
}