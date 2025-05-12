# BookAura

BookAura is a digital book reading platform where different types of users interact with books in different ways. The app is built with Jetpack Compose, following modern Android development practices, and provides a seamless reading and discovery experience.

## Features

- **User Onboarding:** Welcomes users and introduces BookAura's features.
- **Book Library:** Browse and search a curated collection of books.
- **Personalized Experience:** Different user roles (e.g., reader, author, admin) with tailored interactions.
- **Book Details:** View detailed information, including author, genre, and description.
- **Material 3 Design:** Modern, responsive UI with light and dark theme support.

## Screenshots

<p align="center">
  <img src="https://i.postimg.cc/jS6qv6Vr/IMAGE-2025-05-12-12-38-46.jpg" alt="BookAura Onboarding" width="150" />
  <img src="https://i.postimg.cc/4xRNzLS5/IMAGE-2025-05-12-12-38-44.jpg" alt="BookAura Library" width="150" />
  <img src="https://i.postimg.cc/NfmrqHhd/IMAGE-2025-05-12-12-38-49.jpg" alt="BookAura Book Details" width="150" />
  <img src="https://i.postimg.cc/FHxd1XHc/IMAGE-2025-05-12-12-38-47.jpg" alt="BookAura Reading Progress" width="150" />
</p>

## Getting Started

### Prerequisites

- Android Studio Giraffe or newer
- JDK 11 or higher
- Gradle 8.x

### Building and Running

1. **Clone the repository:**
   ```sh
   git clone <your-repo-url>
   cd Book
   ```

2. **Open in Android Studio:**  
   Open the project folder in Android Studio.

3. **Build the project:**  
   The project uses Gradle for build automation. You can build and run the app directly from Android Studio or use the command line:
   ```sh
   ./gradlew assembleDebug
   ```

4. **Run on an emulator or device:**  
   Select a device and click "Run" in Android Studio.

## Project Structure

- `app/src/main/java/` — Main source code for BookAura
- `app/src/test/` — Unit tests
- `.kotlin/` — Kotlin build and session logs

## Customization

- **Add or Edit Books:**  
  Update the book data source in the main Kotlin files under `app/src/main/java/`.
- **User Roles:**  
  Modify user interaction logic in the relevant Kotlin classes to support new roles or permissions.
- **Theming:**  
  Update colors and typography in the theme files under `app/src/main/java/.../ui/theme/`.

## Testing

Unit tests are located in `app/src/test/`. Run tests with:
```sh
./gradlew test
```

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

## Acknowledgements

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)

---

_Created as a digital reading platform project using modern Android and Kotlin best practices._