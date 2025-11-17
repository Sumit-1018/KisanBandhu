# 🌾 KisanBandhu - AI Farming Assistant

**KisanBandhu** is an AI-powered Android app designed for Indian farmers. It provides instant, expert-level agricultural advice by leveraging Google's Gemini API.

Farmers can ask questions about crops, pests, weather, and subsidies using text, voice-to-text, or by uploading a photo of their plant for analysis. The app is built with a modern, beautiful UI featuring gradients and glassmorphism for a simple and accessible user experience.

## ✨ Key Features

* **Multimodal AI Chat:** Get answers from text, voice, or image queries.
* **Gemini API:** Powered by Google's generative AI to provide contextual and accurate information in multiple languages.
* **Modern UI:** A beautiful, edge-to-edge interface with gradients and a clean chat layout.
* **Farmer-First:** Built to be simple and accessible for all users.
* **Secure Login:** Features a splash screen, phone/OTP authentication, and a "Continue as Guest" option.

## 🛠️ Tech Stack

* **Language:** Kotlin
* **AI:** Google Gemini API (gemini-1.5-flash)
* **UI:** Android XML with Material Design 3
* **Architecture:** ViewBinding
* **Asynchronous:** Coroutines for API calls
* **Image Loading:** Coil

## 🚀 How to Run

1.  **Clone the repository:**
    ```sh
    git clone [https://github.com/your-username/KisanBandhu.git](https://github.com/your-username/KisanBandhu.git)
    ```
2.  **Get a Gemini API Key:**
    * Go to [Google AI Studio](https://aistudio.google.com/app/apikey) and create an API key.
3.  **Add the API Key:**
    * Create a file named `local.properties` in the root of your Android Studio project.
    * Add your API key to it:
        ```properties
        API_KEY="YOUR_GEMINI_API_KEY_HERE"
        ```
4.  **Build & Run:**
    * Open the project in Android Studio.
    * Build and run the app on an emulator or a physical device.
```eof
