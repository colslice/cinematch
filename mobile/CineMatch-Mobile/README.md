# 🎬 CineMatch Mobile App (Android)

CineMatch is a modern mobile application designed to help users discover movies tailored to their preferences. Built using **Kotlin** and **Jetpack Compose**, this project focuses on clean UI/UX design, scalable architecture, and a smooth onboarding experience.

---

## Features

### 🔐 Authentication Flow

* Login screen with form validation-ready structure
* Registration screen with structured input fields
* Email verification screen with:

  * Dynamic email passing between screens
  * Resend email timer (cooldown system)

### Onboarding Experience

* Multi-step onboarding flow:

  * **Step 1:** Select streaming services
  * **Step 2:** Select favorite genres
* Interactive selection UI with:

  * Dynamic state management
  * Visual feedback (selected/unselected states)
* Footer navigation with real-time selection count

### UI/UX Design

* Fully built using **Jetpack Compose**
* Dark theme with custom color system
* Responsive mobile-first layout adapted from desktop Figma design
* Smooth animated marquee component
* Reusable UI components (buttons, inputs, selection chips)

---

## Tech Stack

* **Language:** Kotlin
* **UI Framework:** Jetpack Compose
* **Architecture:** MVVM-ready structure
* **Navigation:** Navigation Compose
* **State Management:** Compose State (`remember`, `mutableStateListOf`)
* **Design System:** Custom theme (colors, typography)

---

## 📁 Project Structure

```
com.cop4331.cinematch
│
├── ui/
├── navigation/
│   └── NavGraph.kt
│
│   ├── components/
│   │   ├── PrimaryButton.kt
│   │   ├── GenreButton.kt
│   │   ├── CustomTextField.kt
│   │   ├── ServiceButton.kt
│   │   └── MarqueeBar.kt
│   │
│   ├── screens/
│   │   ├── SplashScreen.kt
│   │   ├── LoginScreen.kt
│   │   ├── RegisterScreen.kt
│   │   ├── EmailVerificationScreen.kt
│   │   ├── SelectServicesScreen.kt
│   │   └── SelectGenresScreen.kt
│   │
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
└── MainActivity.kt
```

---

## ▶️ How to Run the Project

### Prerequisites

* Android Studio (latest version recommended)
* Android SDK installed
* Emulator or physical Android device

---

### ⚙️ Setup Instructions

1. **Clone the repository**

   ```bash
   git clone https://github.com/jakeDaleandro/CineMatch-Mobile.git
   ```

2. **Open in Android Studio**

   * Launch Android Studio
   * Click **"Open"**
   * Select the project folder

3. **Sync Gradle**

   * Android Studio should automatically prompt this
   * If not: click **"Sync Now"** at the top

4. **Run the App**

   * Select an emulator or connect a device
   * Click the **Run ▶️ button**

---

## App Flow

```
Splash Screen
   ↓
Login / Register
   ↓
Email Verification
   ↓
Step 1: Select Services
   ↓
Step 2: Select Genres
   ↓
Step 3: Rate Movies
   ↓
Home Page(recommended movies)
   ↓          ↓         ↓
WatchList   Search   Ratings
```

---

## Future Improvements

* Backend integration (MERN stack APIs)
* Persistent onboarding state (ViewModel)
* User authentication with JWT
* Movie recommendation engine
* Deep linking for email verification
* Animations and transitions between screens

---

## Key Highlights

* Built entirely with **Jetpack Compose (modern Android UI)**
* Focused on **scalable and reusable components**
* Designed with **real-world product UX patterns**
* Backend-ready data structures (arrays for user preferences)

---

## Author

**Jacob Daleandro**

---

## 📌 Notes

This project is part of a full-stack MERN application where this mobile frontend will connect to a backend API for authentication, user data storage, and movie recommendations.
