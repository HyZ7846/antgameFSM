# Ant Game

## 1. Game Logic and Design

### Overview

The Ant Game is a simulation where ants roam around a canvas, interact with different types of terrain (food, water, poison), and return to their home when necessary. The game is designed using JavaFX for the graphical interface and is modularized into several classes to maintain clarity and separation of concerns.

### Classes and Responsibilities

- **GameApplication**: The main class that initializes the game, handles the game loop, and manages the game state.
- **Ant**: Represents an individual ant in the game, handling movement, state transitions, and interactions with the terrain.
- **MovementStyle**: An enum defining the movement styles of ants (`ROAMING` and `RETURNING`).
- **Status**: An enum defining the status of ants (`HUNGRY` and `THIRSTY`).

### Logic

1. **Initialization**:
    - The game starts with an input screen where the user specifies the number of ants to spawn (1-10).
    - The main game scene is set up with a canvas, an ant home, and various terrain elements (food, water, poison).

2. **Game Loop**:
    - The `AnimationTimer` updates the game state at regular intervals.
    - Each frame, the game updates the position of ants, checks for collisions, and redraws the canvas.

3. **Ant Behavior**:
    - Ants move randomly in `ROAMING` mode.
    - Upon encountering food (if `HUNGRY`), brown colored ants switch to `RETURNING` mode to return home.
    - Upon encountering water (if `THIRSTY`), light blue colored ants become `HUNGRY` again.
    - Contact with poison terrain kills the ant instantly.
    - Ants return home in `RETURNING` mode, change their status, and a new ant is spawned.

### Terrain Elements

- **Food**: Brown rectangles that make `HUNGRY` ants switch to `RETURNING` mode.
- **Water**: Light blue rectangles that make `THIRSTY` ants become `HUNGRY`.
- **Poison**: Light green rectangles that instantly kill any ant that contacts them.
- **Ant Home**: A red rectangle at the bottom center of the canvas where ants return to change their status.

## 2. Instructions to Compile and Run

### Prerequisites

- JDK 11 or higher
- JavaFX SDK

### Steps to Compile and Run

1. **Clone the Repository**:
    - Clone the project repository to your local machine.

2. **Compile**:
    - Navigate to the project directory and compile the project using the following command:
      ```bash
      javac --module-path ./libs/javafx-sdk-22.0.1/lib --add-modules javafx.controls,javafx.fxml assign2/ant/*.java
      ```

3. **Run**:
    - Run the game using the following command:
      ```bash
      java --module-path ./libs/javafx-sdk-22.0.1/lib --add-modules javafx.controls,javafx.fxml assign2.ant.HelloApplication
      ```

## 3. State Machine Description

### States

- **HUNGRY**: The default state where ants roam to find food.
- **THIRSTY**: A state where ants roam to find water after returning home with food.

### Transitions

1. **HUNGRY to RETURNING**:
    - Trigger: Ant encounters food terrain.
    - Action: Ant switches to `RETURNING` mode and heads towards home.

2. **RETURNING to THIRSTY**:
    - Trigger: Ant reaches home with food.
    - Action: Ant changes its status to `THIRSTY`.

3. **THIRSTY to HUNGRY**:
    - Trigger: Ant encounters water terrain.
    - Action: Ant changes its status to `HUNGRY`.

4. **Immediate Transitions**:
    - Any state to `DEAD`: Triggered when an ant contacts poison terrain.

## 4. Bugs and Problems

- **Performance Degradation**: The game may slow down over time due to increasing number of ants and the complexity of their interactions.
- **Stuck Ants**: Ants may occasionally get stuck inside terrain elements due to collision detection and movement logic. Further refinement of movement and collision algorithms is needed to resolve this issue completely.
- **UI Issues**: The initial input prompt for the number of ants is basic and does not handle edge cases or invalid inputs robustly.

### Future Improvements

- Optimize rendering and collision detection to improve performance.
- Enhance ant movement logic to prevent ants from getting stuck.
- Improve the user interface and input handling for better user experience.
- Add more behaviors and interactions to make the simulation more complex and interesting.
