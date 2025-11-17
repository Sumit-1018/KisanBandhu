package com.example.kisanbandhu

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate // Import for Night Mode control
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kisanbandhu.databinding.ActivityMainBinding
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val chatAdapter = ChatAdapter()
    private val chatHistory = mutableListOf<ChatItem>()

    private var selectedImageBitmap: Bitmap? = null

    private val generativeModel by lazy {
        val config = generationConfig {
            temperature = 0.7f
        }

        GenerativeModel(
            modelName = "gemini-2.5-pro",
            apiKey = BuildConfig.API_KEY,
            generationConfig = config
        )
    }

    // --- ActivityResult Launchers ---
    private val speechInputLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val speechResult: ArrayList<String>? =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!speechResult.isNullOrEmpty()) {
                binding.etQuery.setText(speechResult[0])
            }
        }
    }

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageBitmap = convertUriToBitmap(it)
            binding.ivImagePreview.setImageBitmap(selectedImageBitmap)
            binding.imagePreviewContainer.visibility = View.VISIBLE
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startVoiceInput()
        } else {
            Toast.makeText(this, "Audio permission is required for voice input.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Force Light Mode (Optional, keeps UI consistent)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // 2. Go Edge-to-Edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 3. Fix Status Bar Icons (Make them Dark/Black)
        val windowInsetsController = WindowCompat.getInsetsController(window, binding.root)
        // true = dark icons (for light background), false = light icons (for dark background)
        windowInsetsController.isAppearanceLightStatusBars = true

        // 4. Handle System Bars & Keyboard Padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())

            view.setPadding(
                systemBars.left,
                systemBars.top, // Ensures chat doesn't go under the status bar
                systemBars.right,
                ime.bottom.coerceAtLeast(systemBars.bottom) // Pushes layout up when keyboard opens
            )
            insets
        }

        setupRecyclerView()
        setupClickListeners()

        addResponseToChat("Hello! I am KisanBandhu. How can I help you today?")
    }

    private fun setupRecyclerView() {
        binding.chatRecyclerView.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                stackFromEnd = true
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnSend.setOnClickListener {
            sendQuery()
        }

        binding.btnVoiceQuery.setOnClickListener {
            checkAudioPermissionAndStartVoice()
        }

        binding.btnAttachImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.btnClosePreview.setOnClickListener {
            clearImagePreview()
        }
    }

    private fun clearImagePreview() {
        selectedImageBitmap = null
        binding.ivImagePreview.setImageBitmap(null)
        binding.imagePreviewContainer.visibility = View.GONE
    }

    private fun checkAudioPermissionAndStartVoice() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                startVoiceInput()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-UK")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your question...")
        }
        try {
            speechInputLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Speech-to-text not available.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendQuery() {
        val queryText = binding.etQuery.text.toString().trim()
        val queryImage = selectedImageBitmap

        if (queryText.isEmpty() && queryImage == null) {
            Toast.makeText(this, "Please type a question or attach an image.", Toast.LENGTH_SHORT).show()
            return
        }

        val userQuery = ChatItem.Query(queryText, queryImage)
        addQueryToChat(userQuery)

        val loadingItem = ChatItem.Loading()
        addLoadingToChat(loadingItem)

        binding.etQuery.text.clear()
        clearImagePreview()

        lifecycleScope.launch {
            try {
                val prompt = content {
                    queryImage?.let { image(it) }
                    // **FIX: Changed prompt to respond in the same language**
                    text("You are KisanBandhu, an expert AI assistant for farmers. " +
                            "Provide clear, actionable advice. " +
                            "Respond in the same language as the user's question. " +
                            "The farmer's question is: $queryText")
                }

                val response = generativeModel.generateContent(prompt)

                removeLoadingFromChat(loadingItem)

                response.text?.let {
                    addResponseToChat(it)
                } ?: addResponseToChat("I received a response, but it was empty.")

            } catch (e: Exception) {
                removeLoadingFromChat(loadingItem)
                Log.e("GeminiQuery", "API Call Failed", e)
                addResponseToChat("Error: ${e.message}")
            }
        }
    }

    // --- Chat List Helper Functions ---

    private fun addQueryToChat(query: ChatItem.Query) {
        chatHistory.add(query)
        updateChatList()
    }

    private fun addResponseToChat(text: String) {
        val response = ChatItem.Response(text)
        chatHistory.add(response)
        updateChatList()
    }

    private fun addLoadingToChat(loading: ChatItem.Loading) {
        chatHistory.add(loading)
        updateChatList()
    }

    private fun removeLoadingFromChat(loading: ChatItem.Loading) {
        chatHistory.remove(loading)
        updateChatList()
    }

    private fun updateChatList() {
        chatAdapter.submitList(chatHistory.toList()) {
            binding.chatRecyclerView.smoothScrollToPosition(chatAdapter.itemCount - 1)
        }
    }

    // --- Utility Function ---

    private fun convertUriToBitmap(uri: Uri): Bitmap? {
        return try {
            val originalBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 512, 512, true)
            originalBitmap.recycle()
            scaledBitmap
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to load image.", Toast.LENGTH_SHORT).show()
            null
        }
    }
}