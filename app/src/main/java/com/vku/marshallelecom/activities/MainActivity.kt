package com.vku.marshallelecom.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.vku.marshallelecom.R
import com.vku.marshallelecom.fragments.ShopProductFragment

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
//        loginToken()
//        var bundle = Bundle()
//        bundle.putInt("product_id", 1)
//
//        var productShowFragment = ProductShowFragment()
//        productShowFragment.arguments = bundle
        var bundle = Bundle()
        bundle.putInt("category_id", 1)
        var shopProductFragment = ShopProductFragment()
        shopProductFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, shopProductFragment).commit()
    }



}