# 🧩 Word Puzzle Game

A fully functional, highly polished Wordscapes-style word puzzle game built natively for Android using modern development standards. It balances an engaging, interactive user experience with a robust, enterprise-grade codebase.

## 📱 Features

*   **Custom Swipe Gestures:** Continuous swipe-to-connect logic built entirely from scratch using Jetpack Compose Canvas.
*   **Dynamic Level Generation:** Crossword grids are generated dynamically based on JSON level data.
*   **Polished UI/UX & Animations:** Features satisfying micro-interactions, flying word animations, Lottie-based fireworks on level completion, and automatic Light/Dark mode theme adaptation.
*   **State Retention:** Game progress is saved locally. The game flawlessly survives configuration changes, screen rotations, and background process deaths.

## 🛠 Tech Stack & Architecture

*   **Language:** Kotlin
*   **UI Toolkit:** Jetpack Compose
*   **Architecture:** MVI / Clean Architecture via ViewModels
*   **Dependency Injection:** Dagger-Hilt
*   **Local Persistence:** Preferences DataStore (for saving current level and found words)
*   **Data Parsing:** Kotlinx Serialization (parsing JSON from assets)

## 🧠 Architectural Choices & Implementations

### 1. Custom Swipe Logic (Jetpack Compose Canvas)
Instead of relying on the legacy Android View system, the letter wheel and dynamic swipe lines were built natively in Compose using `Canvas` and `pointerInput`. 
*   `detectDragGestures` tracks the finger's precise coordinates.
*   Hit-detection mathematics checks if the drag offsets intersect with any letter node's radius.
*   Selected letter coordinates are dynamically added to a `Path()` to draw smooth, rounded connection lines in real-time.

### 2. Level Data Structure
Levels are stored cleanly in a `levels.json` file inside the `assets` folder. This ensures the logic is entirely decoupled from the data. 
*   Each level defines its character pool and a list of `WordPosition` objects.
*   `WordPosition` holds the specific `startX` and `startY` coordinates and direction (`isHorizontal`), allowing for complex, intersecting crossword patterns without a heavy matrix engine.

### 3. State Management
The UI observes a single source of truth (`GameState`) exposed via `StateFlow` from the ViewModel. Side-effects (like Snackbars or Toasts for invalid words) are handled via `SharedFlow`. For complete Android navigation hygiene and lifecycle management, `Preferences DataStore` continuously persists the current `levelIndex` and `foundWords`, allowing users to resume exactly where they left off.

## 🚀 How to Run

1. Clone the repository.
2. Open the project in Android Studio (Ladybug / Koala or latest).
3. Build and Run on an emulator or physical device.
*(Alternatively, a pre-built APK is attached in the Release section).*
