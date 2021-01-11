package com.a5acdhmw

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.a5acdhmw.databinding.ActivityMainBinding
import java.util.concurrent.Executor
import java.util.concurrent.Executors


/**Layout у якому є 3 Views: TextView, ProgressBar (Circle) і Button.
TextView має значення n = 0.
ProgressBar - не видимий і знаходиться по центру layout
При кліку на кнопку Button: TextView і Button зникають, а ProgressBar з’являється на (n+1)/10 секунд.
Після чого видимість об`єктів повертається на попередню. І значення TextView (n) збільшується на 1.*/



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var counterN: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupListeners()
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupListeners() {
        binding.btnLoad.setOnClickListener {
            startUpload()
        }
    }

    private fun startUpload() {
        // Create an executor that executes tasks in the main thread.
        val mainExecutor: Executor = ContextCompat.getMainExecutor(this)

        // Create an executor that executes tasks in a background thread.
        val backgroundExecutor = Executors.newCachedThreadPool()

        // Execute a task in the background thread.
        backgroundExecutor.execute {
            // Update UI on the main thread
            mainExecutor.execute {
                binding.btnLoad.visibility = View.GONE
                binding.tvNCounter.visibility = View.GONE
                binding.progressCircular.visibility = View.VISIBLE
            }

            //run on background thread
            Thread.sleep(this.timeForSleep())
            incrementCounterN()

            // Update UI on the main thread
            mainExecutor.execute {
                binding.tvNCounter.text = "N = $counterN"

                binding.btnLoad.visibility = View.VISIBLE
                binding.tvNCounter.visibility = View.VISIBLE
                binding.progressCircular.visibility = View.GONE
            }
        }
    }


    private fun incrementCounterN() {
        this.counterN += 1
    }

    private fun timeForSleep(): Long {
        var temp = (this.counterN.toLong() + 1) * 1000
        temp /= 10
        return temp
    }

}