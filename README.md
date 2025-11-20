# 🌾 KisanBandhu - AI Farming Assistant

**KisanBandhu** is an AI-powered Android application tailored for the modern Indian farmer. It serves as a pocket assistant, providing instant, expert-level agricultural advice through the power of Google's Gemini API.

## 📖 Overview

KisanBandhu simplifies farming queries by allowing users to interact via text, voice, or images. Whether it's identifying a crop disease from a photo, asking about the best pesticides, or checking subsidy information, the app delivers clear and actionable advice in real-time.

## 📸 Screenshots

| | | |
|:-------------------------:|:-------------------------:|:-------------------------:|
| ![Splash Screen](images/splashScreen) <br> **Splash Screen** | ![Login Page](images/loginPage) <br> **Login Page** | ![Voice Input](images/speechTranscriber) <br> **Voice Input** |
| ![Text Prompt](images/textPromptResponse) <br> **Text Query** | ![Chatbot Response](images/chatbotResponse) <br> **AI Response** | ![Image Attachment](images/imageAttachment) <br> **Image Attachment** |
| ![Image Analysis](images/imagePrompt) <br> **Image Analysis** | | |

## ✨ Key Features

-   **🤖 Multimodal AI Chat:** Interact with the assistant using text, voice-to-text, or image uploads.
-   **🧠 Powered by Gemini:** Utilizes Google's Gemini 1.5 Flash model for accurate, context-aware responses in multiple languages.
-   **🎨 Modern UI:** Features a beautiful, edge-to-edge interface with glassmorphism effects and gradients for a premium user experience.
-   **👨‍🌾 Farmer-First Design:** Simple, accessible, and intuitive interface designed for ease of use.
-   **🔐 Secure Access:** Includes phone/OTP authentication and a "Continue as Guest" option for quick access.

## 🛠️ Tech Stack

-   **Language:** Kotlin
-   **AI Model:** Google Gemini API (gemini-1.5-flash)
-   **UI Framework:** Android XML with Material Design 3
-   **Architecture:** ViewBinding
-   **Concurrency:** Kotlin Coroutines
-   **Image Loading:** Coil

## 🚀 Getting Started

Follow these steps to set up and run the project locally.

### Prerequisites
-   Android Studio Ladybug or newer
-   Basic knowledge of Android development

### Installation

1.  **Clone the repository**
    ```bash
    git clone [https://github.com/Sumit-1018/KisanBandhu.git](https://github.com/Sumit-1018/KisanBandhu.git)
    ```

2.  **Obtain a Gemini API Key**
    -   Visit [Google AI Studio](https://aistudio.google.com/app/apikey) to generate your free API key.

3.  **Configure the API Key**
    -   Navigate to the root directory of the project.
    -   Create a file named `local.properties` (if it doesn't already exist).
    -   Add your API key to the file:
        ```properties
        API_KEY="YOUR_GEMINI_API_KEY_HERE"
        ```

4.  **Build and Run**
    -   Open the project in Android Studio.
    -   Sync the Gradle files.
    -   Run the application on an Android Emulator or a physical device.

## 🤝 Contributing

Contributions are welcome! If you'd like to improve KisanBandhu, please follow these steps:

1.  Fork the repository.
2.  Create a new branch (`git checkout -b feature/YourFeature`).
3.  Commit your changes (`git commit -m 'Add some feature'`).
4.  Push to the branch (`git push origin feature/YourFeature`).
5.  Open a Pull Request.

## 👤 Author

**Sumit**
-   GitHub: [@Sumit-1018](https://github.com/Sumit-1018)
