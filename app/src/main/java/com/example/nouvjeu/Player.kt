package com.example.nouvjeu


import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class Player : ImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    init {
        this.setImageResource(R.drawable.helico)
    }

    fun moveUp() {
        this.translationY -= 10
    }

    fun moveDown() {
        this.translationY += 10
    }

}
