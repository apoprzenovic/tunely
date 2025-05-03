# Tunely 

Tunely is an Android application where users guess the title of a song based on a short preview fetched from the iTunes API. It's a fun music trivia game inspired by Wordle.
Press Play, hear a 30-second iTunes preview, and type the song title in six tries or less.

## Features

| Screen          | What you can do                                                                                                                                                                                                                                              |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Game**        | • Stream a random 30-s preview from the iTunes Search API<br>• On-screen keyboard (mobile-friendly)<br>• Six attempts per round, 9-char titles only → fast pacing<br>• Instant hint & colour feedback grid<br>• Auto-scored points (10…5) saved to the cloud |
| **Leaderboard** | • Top 10 players worldwide<br>• Trophy, silver & bronze backgrounds for the podium                                                                                                                                                                           |
| **Profile**     | • View username + total points<br>• Edit your bio, saved only on **Save**<br>• Secure log-out                                                                                                                                                                |
| **Auth**        | • Firebase e-mail sign-in & sign-up flows<br>• Form validation, loading spinners, error toasts                                                                                                                                                               |
| **Theme**       | • Material 3 + custom pastel palette<br>• Animated buttons that darken on press                                                                                                                                                                              |

## Technologies Used

*   **Language:** Kotlin
*   **UI:** 100% Jetpack Compose UI
*   **Architecture:** MVVM (ViewModel, Repository, State)
*   **Dependency Injection:** Hilt
*   **Networking:** Retrofit (for iTunes API), Coroutines, OkHttp, Gson
*   **Player:** ExoPlayer (Media3) for audio
*   **Authentication:** Firebase Authentication
*   **Data Persistence:** Firebase Firestore
*   **API:** Apple iTunes Search API

## Architecture at a glance
```text
data/
 ├─ remote/         ← iTunes Search REST
 ├─ repository/     ← ITunesRepository | UserRepository | LeaderboardRepository
 └─ model/          ← Track | User

player/              ← thin ExoPlayer wrapper

ui/
 ├─ screens/
 │   ├─ game/       ← GameScreen + sub-components
 │   ├─ leaderboard/
 │   ├─ profile/
 │   └─ auth/       ← Login / Sign-Up
 ├─ components/     ← reusable AnimatedColorButton, ScreenTopBar…
 └─ theme/          ← pastel colour palette

navigation/          ← enum-based destinations
util/                ← constants, mappers, Resource<T>
```

## Prerequisites
| Tool                      | Version                           |
| ------------------------- | --------------------------------- |
| Android Studio            | **Giraffe** (or newer)            |
| Kotlin                    | 1.9.x                             |
| Android Gradle Plugin     | 8.2                               |
| JDK                       | 17                                |
| Android device / emulator | API 34+ (minSdk 34, targetSdk 35) |


## Configuration
| File               | Purpose                                             |
| ------------------ | --------------------------------------------------- |
| `Constants.kt`     | – Track limit, guess count, iTunes base-URL         |
| `GamePalette.kt`   | – All pastel colours in one place                   |
| `ITunesRepository` | – Random letter + offset logic (feel free to tweak) |

No iTunes key is required – the public Search API exposes 30-second previews out of the box.

## Build & run
```bash./gradlew :app:installDebug```

## Licence
MIT © 2025 Arnes Poprzenovic
