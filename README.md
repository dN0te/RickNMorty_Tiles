
# Rick and Morty Game

## Overview
This project is a simple yet engaging Android game where the player controls Morty to avoid obstacles represented by Rick, falling from the top of the screen. The game features score tracking, a life count, vibrations on collisions, a game over screen, a background soundtrack, and additional game modes like Tilt Control and 2x Speed.

## Features
- **Morty's Movement**: Control Morty's movement left and right using on-screen buttons.
- **Falling Obstacles**: Rick obstacles fall from the top in 5 predefined lanes.
- **Score Tracking**: The score increments over time based on survival duration.
- **Life System**: Lose a life on collision with Rick. The game ends when all lives are lost.
- **Vibration on Collision**: The device vibrates on collisions to provide tactile feedback.
- **Game Over Screen**: Displays when all lives are lost, showing the final score.
- **Background Soundtrack**: A looping background soundtrack with adjustable volume.
- **Tilt Control Mode**: Control Morty's movement by tilting the device.
- **2x Speed Mode**: Increase the difficulty by doubling the falling speed of obstacles.
- **Scoreboard with Google Maps Integration**: Display player scores along with their locations on a Google Map.

## Requirements
- Android Studio
- Minimum SDK version 16
- Target SDK version 30
- Google Maps API key (for Maps integration)

## Installation
1. Clone the repository:
    \`\`\`sh
    git clone <repository-url>
    \`\`\`
2. Open the project in Android Studio:
    - Select "Open an existing Android Studio project".
    - Navigate to the cloned repository and select it.
3. Build the project to ensure all dependencies are installed:
    - Go to \`Build\` > \`Rebuild Project\`.
4. Obtain a Google Maps API key:
    - Go to the [Google Cloud Console](https://console.cloud.google.com/).
    - Create a new project or select an existing one.
    - Enable the Maps SDK for Android.
    - Generate an API key.
5. Add the Google Maps API key to your project:
    - Open the \`AndroidManifest.xml\` file.
    - Add the following within the \`<application>\` tag:
    \`\`\`xml
    <meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="YOUR_API_KEY_HERE"/>
    \`\`\`
6. Run the project on an emulator or physical device:
    - Click the green play button or go to \`Run\` > \`Run 'app'\`.

## Usage
1. **Start Screen**:
    - **Normal Mode**: Tap the "Normal Mode" button to start the game with standard settings.
    - **Tilt Control**: Tap the "Tilt Control" button to start the game with tilt-based controls.
    - **2x Speed**: Tap the "2x Speed" button to start the game with increased obstacle speed.
    - **Scoreboard**: Tap the "Scoreboard" button to view the high scores.
2. **Game Screen**:
    - Use the left and right buttons to move Morty and avoid the falling Rick obstacles.
    - The score increases over time. Avoid collisions to maintain lives.
    - The game vibrates and reduces a life on collision.
    - When all lives are lost, the game ends and the game over screen is displayed.
3. **Scoreboard Screen**:
    - View the top scores along with the players' locations displayed on a Google Map.

## Project Structure
- \`MainActivity.java\`: The main activity that handles user interactions and displays the game UI.
- \`GameManager.java\`: Manages game logic, including Morty's movement, score updates, obstacle updates, and collision detection.
- \`SoundtrackManager.java\`: Manages the background soundtrack.
- \`StartActivity.java\`: Handles the start screen UI and navigation to different game modes.
- \`TiltControlActivity.java\`: Manages the tilt control game mode.
- \`X2SpeedActivity.java\`: Manages the 2x speed game mode.
- \`ScoreboardActivity.java\`: Displays the scoreboard with high scores and player locations on a Google Map.
- \`MapsActivity.java\`: Manages the Google Maps integration for displaying player locations.
- \`activity_main.xml\`: Layout file defining the game UI, including Morty's lanes, Rick's obstacles, score display, and control buttons.
- \`activity_start.xml\`: Layout file defining the start screen UI, including buttons for starting the game and accessing other modes.
- \`activity_scoreboard.xml\`: Layout file defining the scoreboard UI, including the Google Map.
- \`drawable/\`: Contains images for the game background, Rick, Morty, and heart icons for lives.
- \`raw/\`: Contains audio files like the background soundtrack.

## Key Classes and Methods
### MainActivity
- \`onCreate()\`: Initializes the game, sets up listeners for button clicks, and starts the soundtrack.
- \`onScoreUpdated()\`: Updates the displayed score.
- \`onLifeUpdated()\`: Updates the displayed lives and handles game over logic.

### GameManager
- \`moveMortyRight()\`: Moves Morty one lane to the right.
- \`moveMortyLeft()\`: Moves Morty one lane to the left.
- \`startObstacleTimer()\`: Starts the timer for updating obstacles and checking collisions.
- \`checkCollision()\`: Checks for collisions between Morty and Rick.
- \`refreshRickVisibility()\`: Updates the visibility of Rick images based on the game state.

### SoundtrackManager
- \`startSoundtrack(Context context)\`: Starts the background soundtrack and sets the volume to 70%.
- \`stopSoundtrack()\`: Stops and releases the background soundtrack.

### StartActivity
- \`onCreate()\`: Initializes the start screen, starts the soundtrack, and sets up listeners for the game mode buttons.

### TiltControlActivity
- \`onCreate()\`: Sets up the tilt control game mode.
- \`onSensorChanged()\`: Handles device tilt for controlling Morty's movement.

### X2SpeedActivity
- \`onCreate()\`: Sets up the 2x speed game mode.
- \`increaseSpeed()\`: Increases the speed of falling obstacles.

### ScoreboardActivity
- \`onCreate()\`: Displays the high scores and initializes the map.
- \`initMap()\`: Initializes the Google Map for displaying player locations.

### MapsActivity
- \`onMapReady(GoogleMap googleMap)\`: Called when the map is ready to be used.
- \`addMarker(LatLng location, String title)\`: Adds a marker on the map at the specified location with a given title.

## Assets
- \`rnm_background.png\`: Background image for the game.
- \`rick.png\`: Image used for Rick obstacles.
- \`morty.png\`: Image used for Morty.
- \`heart.png\`: Image used for displaying lives.
- \`evilmorty_st.mp3\`: Background soundtrack for the game.

## License
This project is licensed under the MIT License. See the LICENSE file for details.
