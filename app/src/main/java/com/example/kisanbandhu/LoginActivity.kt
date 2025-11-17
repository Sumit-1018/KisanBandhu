package com.example.kisanbandhu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kisanbandhu.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- OTP Button Click ---
        binding.btnGetOtp.setOnClickListener {
            if (!validateInput()) {
                return@setOnClickListener
            }

            // SUCCESS
            toast("Login Successful")

            // TODO: Add real OTP logic here
            // For now, we go directly to OtpActivity
            startActivity(Intent(this, OtpActivity::class.java))
            finish()
        }

        // --- Guest Login Click ---
        binding.tvGuestLogin.setOnClickListener {
            toast("Continuing as Guest")
            // Go directly to the main app
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun validateInput(): Boolean {
        val name = binding.etUserName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val ageText = binding.etAge.text.toString().trim()

        // VALIDATION
        return when {
            name.isEmpty() -> {
                toast("Enter your name")
                false
            }
            phone.length != 10 -> {
                toast("Enter valid 10-digit phone number")
                false
            }
            ageText.isEmpty() -> {
                toast("Enter your age")
                false
            }
            ageText.toIntOrNull() == null -> {
                toast("Age must be a number")
                false
            }
            ageText.toInt() !in 1..120 -> {
                toast("Enter a valid age (1–120)")
                false
            }
            else -> {
                true // All checks passed
            }
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}