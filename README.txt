                 ========================================================
                                 Rick and Morty Game
                 ========================================================

Overview:
---------
This project is a simple Android game where the player controls Morty to avoid obstacles (Rick) falling from the top of the screen. The game includes score tracking and life count, with vibrations on collisions and a game over screen.

Features:
---------
- Control Morty's movement left and right using buttons.
- Rick obstacles fall from the top in 5 lanes.
- Score increments over time.
- Lose a life on collision with Rick.
- Game over when all lives are lost.

Requirements:
-------------
- Android Studio
- Minimum SDK version 16
- Target SDK version 30

Installation:
-------------
1. Clone the repository:
git clone <repository-url>
2. Open the project in Android Studio.
3. Build the project to ensure all dependencies are installed.
4. Run the project on an emulator or physical device.

Usage:
------
- Use the left and right buttons to move Morty and avoid the falling Rick obstacles.
- The score increases over time, and the game will vibrate and reduce lives on collision.
- The game ends when all lives are lost, showing a game over screen.

Project Structure:
------------------
- `MainActivity.java`: The main activity that handles user interactions and displays the game UI.
- `GameManager.java`: Manages game logic, including Morty's movement, score updates, obstacle updates, and collision detection.
- `activity_main.xml`: Layout file defining the game UI, including Morty's lanes, Rick's obstacles, score display, and control buttons.
- `drawable/`: Contains images for the game background, Rick, Morty, and heart icons for lives.

Key Classes and Methods:
------------------------
- `MainActivity`
- `onCreate()`: Initializes the game and sets up listeners for button clicks.
- `onScoreUpdated()`: Updates the displayed score.
- `onLifeUpdated()`: Updates the displayed lives and handles game over logic.
- `GameManager`
- `moveMortyRight()`: Moves Morty one lane to the right.
- `moveMortyLeft()`: Moves Morty one lane to the left.
- `startObstacleTimer()`: Starts the timer for updating obstacles and checking collisions.
- `checkCollision()`: Checks for collisions between Morty and Rick.
- `refreshRickVisibility()`: Updates the visibility of Rick images based on the game state.

Assets:
-------
- `rnm_background.png`: Background image for the game.
- `rick.png`: Image used for Rick obstacles.
- `morty.png`: Image used for Morty.
- `heart.png`: Image used for displaying lives.

License:
--------
This project is licensed under the MIT License. See the LICENSE file for details.
