# Playground App (Kotlin + Jetpack Compose)

A small experimental Android application built in Kotlin, designed as a personal playground to explore architecture patterns, state management, Jetpack Compose, commands, navigation, and Android platform features such as media handling (gallery and camera).

The goal of this project is to have a minimal but well-structured environment to test ideas quickly while maintaining clean code separation and domain clarity.

---

# Overview

This app provides a series of simple features that demonstrate:

- A clean architecture based on a modern adaptation of MVC.
- Immutable state modeled in Kotlin domain classes.
- Centralized state management through a store with undo/redo support.
- A Command-based control layer (instead of ViewModel or controllers).
- UI built entirely with Jetpack Compose.
- Interaction with Android system components: Photo Picker, Camera, Intents, Clipboard, Browser.
- Navigation via a bottom navigation bar.
- Dependency management through a manually implemented Service Locator.

This project is intentionally simple, focused on clarity and experimentation rather than production-level complexity.

---

# Architecture

The architecture follows a structured **MVC approach**, adapted for Kotlin and Compose:

- **Model**: Immutable domain and application state. Pure Kotlin.
- **Control**: Commands that modify the state; manages business actions explicitly.
- **View**: Jetpack Compose UI observing the state.
- **SharedKernel**: Core utilities such as routing, shared primitives, and general helpers.

The result is a predictable, testable, and decoupled system.

---

# Modules / Layers

## Model
Contains all domain objects and the global application state.

- `AppState`: Immutable state for the whole application.
- `Counter`: Domain logic for a counter (validation included).
- `MediaData`: Sealed type representing media coming either from the gallery or from the camera.
- Domain types never depend on Android or UI; only pure Kotlin logic.

## Control
Encapsulates actions and application logic using the **Command Pattern**.

- `Command`: Interface representing a user or system action.
- `CommandRunner`: Executes commands and updates state/history.
- Counter Commands:
  - `IncCounter`
  - `ResetCounter`
- Media Commands:
  - `SetPickedImage`
  - `SetCapturedPreview`
- Undo/Redo Commands:
  - `UndoCommand`
  - `RedoCommand`

Commands update state in a controlled and reversible way.

## View
Jetpack Compose components observing `AppState`.

Main screens include:

- `CounterView`: UI for the counter feature.
- `MediaView`: UI for choosing a photo or taking a picture.
- `PlaygroundView`: Small demos of system APIs (clipboard, share intent, open URL, etc.).
- `AppRoot`: Navigation graph, bottom navigation bar, initialization of commands.

Views are stateless: they only react to the state and invoke commands via a provided command map.

## SharedKernel
Contains reusable components:

- `Router`: Defines navigation routes.
- Strategy helpers for handling media.
- Shared small utilities not tied to any particular feature.

## Core
Low-level functionality unrelated to domain logic:

- `AppStateStore`: Wraps a `MutableStateFlow` and exposes the global immutable state.
- `HistoryStore`: Offers undo and redo functionality, using two internal stacks.

Together they implement a small Redux-like reactive store.

## Dependency Injection
A very lightweight DI approach is used:

- `ServiceLocator`: Provides `AppStateStore`, `HistoryStore`, and `CommandRunner`.

The structure allows an easy migration to Hilt if needed later.

---

# Patterns Used

## Command Pattern
Every interaction (increment, reset, set image, undo, redo…) is expressed as a Command that:

1. Reads current state.
2. Computes the new state.
3. Updates `AppStateStore`.
4. Optionally adds the previous state to `HistoryStore`.

This makes behavior explicit, predictable, and easy to reverse.

## Strategy Pattern
Used for differentiating how the app processes images depending on their origin.

Example implementations:

- `GalleryStrategy`
- `CameraPreviewStrategy`

The UI simply delegates behavior through the strategy interface, without knowing the concrete type.

## Immutable State
All domain models are immutable.  
State is replaced rather than mutated, enabling predictable state transitions and undo/redo functionality.

## Declarative UI (Compose)
Views are pure functions of the state.  
State changes automatically trigger recomposition.

## MVC (Modernized)
- Model → Immutable AppState and domain.
- View → Compose UI.
- Controller → Replaced with a Command-based Control layer.

This structure keeps responsibilities explicit and avoids mixing state and UI logic.

---

# Features

## Counter Feature
- Increase counter value.
- Decrease counter value.
- Reset counter.
- Undo and Redo support.
- Full validation of negative operations.
- Domain state never mutates directly.

## Media Feature
- Select an image from the gallery (Photo Picker).
- Capture a quick picture using the camera preview.
- Distinguish source type using Strategy Pattern.
- Display images with Coil.
- Undo/Redo for media changes.

## Android Playground
Small demonstrations using Android platform APIs:

- Share text via Intent.
- Copy text to clipboard.
- Open external URL using `UriHandler`.
- Show Date Picker Dialog.
- Trigger haptic feedback.

Useful as references to learn or test simple Android interactions.

## Navigation
- Bottom bar navigation.
- Two main screens: Counter and Media.
- Architecture ready for more screens.

---

# Technologies Used

- Kotlin
- Jetpack Compose (Material 3)
- Navigation Compose
- Kotlin Coroutines + StateFlow
- Coil (for image loading)
- ActivityResultContracts (Photo Picker, Camera Preview)
- Gradle Kotlin DSL
- JUnit (for domain and command tests)

---

# Testing

The system is designed to allow easy unit testing, thanks to:

- Immutable domain state
- Commands without Android dependencies
- AppStateStore and HistoryStore being pure Kotlin

