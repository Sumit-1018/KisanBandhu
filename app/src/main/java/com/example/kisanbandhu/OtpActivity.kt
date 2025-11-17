package com.example.kisanbandhu

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kisanbandhu.databinding.ActivityOtpBinding

class OtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupOtpInputs()

        binding.btnVerifyOtp.setOnClickListener {
            val otp = binding.otp1.text.toString() +
                    binding.otp2.text.toString() +
                    binding.otp3.text.toString() +
                    binding.otp4.text.toString()

            if (otp.length != 4) {
                show("Enter a valid 4-digit OTP")
                return@setOnClickListener
            }

            show("OTP Verified")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setupOtpInputs() {
        val boxes = listOf(binding.otp1, binding.otp2, binding.otp3, binding.otp4)

        for (i in boxes.indices) {
            boxes[i].addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrEmpty() && i < boxes.size - 1) {
                        boxes[i + 1].requestFocus()
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    private fun show(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
