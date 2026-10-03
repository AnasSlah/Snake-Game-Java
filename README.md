# 🐍 Snake Game - Java Swing

A classic Snake game built with Java (Swing), featuring a custom UI and unique gameplay mechanics that make it more engaging and visually appealing than the traditional version.

> **[Drag and drop your game screenshot here]**

## ✨ Features
* **Custom UI Panel:** A built-in side panel displaying the live score and bonus timer.
* **Smart Bonus System:** A green bonus apple appears every 5 red apples, granting 10 points (instead of 5) and increasing the snake's length by 3 segments at once. It features a live on-screen countdown timer and disappears after 10 seconds.
* **Smooth Movement:** Implements Double Buffering to prevent screen tearing and ensure smooth gameplay.
* **Dynamic Visual Details:** 
  * The snake's head features dynamic "eyes" that change orientation based on the movement direction (horizontal or vertical).
  * Apples are drawn with a rounded shape and a small "stem" for a more realistic look.
* **Safe Food Spawning:** A custom algorithm ensures that new apples never spawn on top of the snake's body or head.
* **Precise Grid Alignment:** Fixed window dimensions (`setResizable(false)`) and calculated insets to maintain a perfect grid layout.

## 🛠️ Limitations & Future Improvements
* **No High Score Tracking:** The score resets to zero upon losing and is not permanently saved to a file or database.
* **Static Difficulty:** The game speed is fixed (150ms) and does not increase as the snake grows, which might reduce the challenge in late-game stages.
* **No Audio:** The game currently lacks sound effects for eating apples or getting a "Game Over".

## 🚀 How to Run
1. Ensure you have the Java Development Kit (JDK) installed on your machine.
2. Download or clone this repository containing `windowGame.java`.
3. Compile and run the file. 
4. Use the **Arrow Keys** to move the snake, and press **ENTER** to restart when the game is over.<img width="1280" height="800" alt="image" src="https://github.com/user-attachments/assets/92c8e780-27da-4c22-a6a0-7a2ab5c27065" />
