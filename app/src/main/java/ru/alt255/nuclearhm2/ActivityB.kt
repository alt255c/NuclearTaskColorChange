package ru.alt255.nuclearhm2

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import ru.alt255.nuclearhm2.databinding.ActivityBBinding

class ActivityB : AppCompatActivity() {
    private lateinit var binding: ActivityBBinding
    private var currentColor: String? = null
    private val defaultColor = 0xFF03DAC5.toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        binding = ActivityBBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        handleIntent(intent)
        restoreState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_COLOR, currentColor)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        restoreState(savedInstanceState)
    }

    private fun initViews() {
        binding.root.setBackgroundColor(defaultColor)
        binding.btnOpenC.setOnClickListener {
            openActivityC()
        }
    }

    private fun handleIntent(intent: Intent?) {
        intent?.getStringExtra(KEY_COLOR)?.takeIf { it.isNotEmpty() }?.let { colorHex ->
            currentColor = colorHex
            updateBackgroundColor()
        }
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        savedInstanceState?.getString(KEY_COLOR)?.let {
            currentColor = it
            updateBackgroundColor()
        }
    }

    private fun updateBackgroundColor() {
        try {
            currentColor?.let { color ->
                binding.root.setBackgroundColor(android.graphics.Color.parseColor(color))
            } ?: run {
                binding.root.setBackgroundColor(defaultColor)
            }
        } catch (e: Exception) {
            binding.root.setBackgroundColor(defaultColor)
        }
    }

    private fun openActivityC() {
        Intent(this, ActivityC::class.java).apply {
            putExtra(KEY_COLOR, currentColor)
            startActivity(this)
        }
    }

    companion object {
        const val KEY_COLOR = "color"
    }
}