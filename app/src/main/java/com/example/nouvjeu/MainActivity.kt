package com.example.nouvjeu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {


    private lateinit var backgroundImage1: ImageView
    private lateinit var backgroundImage2: ImageView
    private var animator: ValueAnimator? = null
    private val enemies = mutableListOf<Enemy>()
    private lateinit var player: Player
    private val handler = Handler(Looper.getMainLooper())
    private val moveUpTask = object : Runnable {
        override fun run() {
            player.moveUp()
            handler.postDelayed(this, 20)
        }
    }
    private val moveDownTask = object : Runnable {
        override fun run() {
            player.moveDown()
            handler.postDelayed(this, 20)
        }
    }
    private val updateEnemiesTask = object : Runnable {
        override fun run() {
            enemies.forEach { it.updatePosition() }
            handler.postDelayed(this, 20)
        }
    }
    private val spawnEnemyTask = object : Runnable {
        override fun run() {
            val mainLayout: ConstraintLayout = findViewById(R.id.mainLayout)
            val screenHeight = resources.displayMetrics.heightPixels
            val enemy = Enemy(this@MainActivity, null).apply {
                positionX = 2000 // Spawn at the right edge of the screen
                positionY = (0..screenHeight).random() // Random Y position
                speed = (45..55).random()
            }
            enemies.add(enemy)
            mainLayout.addView(enemy)

            handler.postDelayed(this, 500) // Spawn a new enemy every 2 seconds
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        player = findViewById(R.id.player)
        backgroundImage1 = findViewById(R.id.backgroundImage)
        backgroundImage2 = findViewById(R.id.backgroundImage2)
        val playButton: Button = findViewById(R.id.play)
        val mainLayout: ConstraintLayout=findViewById(R.id.mainLayout)

        mainLayout.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    handler.removeCallbacks(moveDownTask)
                    handler.post(moveUpTask)
                }
                MotionEvent.ACTION_UP -> {
                    handler.removeCallbacks(moveUpTask)
                    handler.post(moveDownTask)
                }
            }
            true
        }

        playButton.setOnClickListener {
            startScrollingBackground()
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(moveUpTask)
        handler.removeCallbacks(moveDownTask)
        handler.removeCallbacks(updateEnemiesTask)
        handler.removeCallbacks(spawnEnemyTask)
    }

    private fun startScrollingBackground(){
        val textView1: TextView = findViewById(R.id.gametitle)
        val textView2: TextView = findViewById(R.id.polytech2024)
        val playButton: Button = findViewById(R.id.play)

        animator?.cancel()
        playButton.visibility = View.GONE
        textView1.visibility = View.GONE
        textView2.visibility = View.GONE

        animator = ValueAnimator.ofFloat(0.0f, backgroundImage1.width.toFloat()).apply {
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            duration = 10000

            addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                backgroundImage1.translationX = -progress
                backgroundImage2.translationX = -progress + backgroundImage1.width
            }

            start()
        }
       handler.post(updateEnemiesTask)
        handler.post(spawnEnemyTask)
    }

    fun stopScrollingBackground(){
        animator?.cancel()
    }
}