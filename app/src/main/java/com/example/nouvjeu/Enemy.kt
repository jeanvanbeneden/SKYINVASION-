package com.example.nouvjeu

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.widget.ImageView

class Enemy(context: Context, attrs: AttributeSet?) : ImageView(context, attrs) {
    var speed : Int = 20
    var positionX : Int = 0
    var positionY : Int = 0

    init{
        val enemyBitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.enemy)
        setImageBitmap(enemyBitmap)
    }

    fun updatePosition(){
        positionX -= speed
        x= positionX.toFloat()
        y= positionY.toFloat()
    }

}
