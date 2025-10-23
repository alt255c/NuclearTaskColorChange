package ru.alt255.nuclearhm2

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import ru.alt255.nuclearhm2.databinding.ActivityCBinding

class ActivityC : AppCompatActivity() {
    private lateinit var binding: ActivityCBinding
    private var currentColor: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        binding = ActivityCBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleIntent(intent)
        initViews()
    }

    private fun handleIntent(intent: Intent?) {
        currentColor = intent?.getStringExtra(KEY_COLOR)
    }

    private fun initViews() {
        binding.root.setBackgroundColor(0xFFFFDE03.toInt())
        binding.btnOpenA.setOnClickListener {
            openActivityA()
        }
    }

    private fun openActivityA() {
        Intent(this, ActivityA::class.java).apply {
            putExtra(KEY_COLOR, currentColor)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(this)
        }
    }

    companion object {
        const val KEY_COLOR = "color"
    }
}