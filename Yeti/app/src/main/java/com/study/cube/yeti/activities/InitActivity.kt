package com.study.cube.yeti.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.study.cube.yeti.MainActivity
import com.study.cube.yeti.R

class InitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)
        startLoading()
    }

    fun startLoading() {
        Handler().postDelayed({
//            startActivity(Intent(this,MainActivity::class.java))
            startActivity(Intent(this, MainActivity::class.java))
        }, 700)
    }
}
