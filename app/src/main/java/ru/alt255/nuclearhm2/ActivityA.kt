package ru.alt255.nuclearhm2

import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.alt255.nuclearhm2.databinding.ActivityABinding
import kotlin.random.Random

class ActivityA : AppCompatActivity() {
    private lateinit var binding: ActivityABinding
    private var currentColor: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSecurityFlags()
        binding = ActivityABinding.inflate(layoutInflater)
        setContentView(binding.root)

        restoreState(savedInstanceState)
        initViews()

        if (savedInstanceState == null) {
            handleIntent(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_COLOR, currentColor)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        restoreState(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        val newColor = intent.getStringExtra(KEY_COLOR)
        if (!newColor.isNullOrEmpty() && validateColor(newColor)) {
            currentColor = newColor
            binding.etColor.setText(newColor)
            updateBackgroundColor()
        }
    }

    private fun setupSecurityFlags() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setTurnScreenOn(true)
        setShowWhenLocked(true)

        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        if (!powerManager.isInteractive) {
            val wakeLock = powerManager.newWakeLock(
                PowerManager.FULL_WAKE_LOCK or
                        PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "ActivityA:WakeLock"
            )
            wakeLock.acquire(1000)
        }
    }

    private fun initViews() {
        updateBackgroundColor()

        binding.btnOpenB.setOnClickListener {
            openActivityB()
        }

        binding.btnGenerateColor.setOnClickListener {
            generateColor()
        }

        binding.btnApplyColor.setOnClickListener {
            applyColorFromInput()
        }
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        savedInstanceState?.getString(KEY_COLOR)?.let { savedColor ->
            currentColor = savedColor
            binding.etColor.setText(savedColor)
            updateBackgroundColor()
        }
    }

    private fun handleIntent(intent: Intent?) {
        val newColor = intent?.getStringExtra(KEY_COLOR)
        if (!newColor.isNullOrEmpty() && validateColor(newColor)) {
            currentColor = newColor
            binding.etColor.setText(newColor)
            updateBackgroundColor()
        }
    }

    private fun openActivityB() {
        if (!applyColorFromInput()) {
            return
        }

        Intent(this, ActivityB::class.java).apply {
            putExtra(KEY_COLOR, currentColor)

            startActivity(this)
        }
    }

    private fun generateColor() {
        val color = String.format("#%06X", Random.nextInt(0xFFFFFF))
        currentColor = color
        binding.etColor.setText(color)
        updateBackgroundColor()
    }

    private fun applyColorFromInput(): Boolean {
        val colorText = binding.etColor.text.toString().trim()

        if (colorText.isEmpty()) {
            return true
        }

        if (!validateColor(colorText)) {
            Toast.makeText(this, "Некорректный формат цвета. Используйте формат #RRGGBB", Toast.LENGTH_SHORT).show()
            return false
        }

        currentColor = colorText
        updateBackgroundColor()
        return true
    }

    private fun updateBackgroundColor() {
        try {
            currentColor?.let { color ->
                binding.root.setBackgroundColor(android.graphics.Color.parseColor(color))
            } ?: run {
                binding.root.setBackgroundColor(0xFFBB86FC.toInt())
            }
        } catch (e: Exception) {
            binding.root.setBackgroundColor(0xFFBB86FC.toInt())
        }
    }

    private fun validateColor(color: String): Boolean {
        return color.matches(Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})\$"))
    }

    companion object {
        const val KEY_COLOR = "color"
    }
}