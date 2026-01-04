# Android stencil
A modern, multi-module Android template featuring Jetpack Compose and custom Gradle tasks for automated feature generation

## About
This repository serves as a comprehensive template for building Android applications using the latest technologies and best practices. It is designed to help developers kickstart their projects with a solid foundation, featuring a modular architecture, Jetpack Compose for UI development, Hilt for dependency injection, and custom Gradle tasks to streamline the creation of new features.

## How to use
1. **Clone the repository**: Start by cloning this repository to your local machine.
2. **Repackage the project**: Optionally, you can repackage the project to use your own domain and project name by running the following Gradle task:
   ```
   ./gradlew repackageProject --newPrefix="com.yourdomain" --newPrijectName=yourprojectname
   ```
   It will replace all occurrences of the old package name and project name with the new ones you provide. Replace `com.yourdomain` and `yourNewProjectName` with the desired values for your project.
2. **Generate new feature**: Use the provided Gradle task to generate a new feature module. You can do this by running the following command in your terminal:
   ```
   ./gradlew generateFeature --featureName=YourFeatureName
   ```
   Replace `YourFeatureName` with the desired name for your new feature. The feature will be created under the `feature` directory. You can also make use of additional parameter `--addToNavBar` to automatically integrate the new feature into the app's navigation bar.
3. **Sync the project**: After generating the new feature, sync your project with Gradle files (`File > Sync Project with Gradle Files` in Android Studio) to make Gradle aware of the new module.
4. **Customize your feature**: The generated feature module will include boilerplate code for a ViewModel (using MVI architecture), a Composable screen, dependency injection and navigation setup and will be ready for you to customize and expand upon.

## Feature structure
Each generated feature module will have the following structure:
```
example/
├── build.gradle.kts              // Feature Gradle build configuration
├── consumer-rules.pro            // ProGuard rules for consumers of the feature
├── proguard-rules.pro            // ProGuard rules for the feature
└── src/
    └── main/
        ├── AndroidManifest.xml   // Android manifest file for the feature (empty by default)
        ├── res/
        │   └── values/
        │       └── strings.xml   // String resources for the feature
        └── kotlin/
            └── com/app/feature/example/
                ├── data/         // Data layer implementation
                │   ├── di/
                │   │   └── ExampleDataModule.kt       // Data layer dependency injection
                │   └── repository/
                │       └── ExampleRepositoryImpl.kt   // Repository implementation
                ├── domain/       // Domain layer implementation
                │   └── repository/
                │       └── ExampleRepository.kt       // Repository interface
                └── presentation/ // Presentation layer implementation
                    ├── di/
                    │   └── ExampleNavigationModule.kt // Navigation dependency injection
                    ├── mvi/      // State management using MVI pattern
                    │   ├── ExampleContract.kt         // State, Intent, and Effect definitions
                    │   └── ExampleViewModel.kt        // ViewModel implementation
                    ├── navigation/
                    │   ├── ExampleNavigationDefinition.kt // Graph registration
                    │   └── ExampleRoutes.kt           // Serializable route definitions
                    └── screen/
                        └── ExampleScreen.kt           // Composable screen implementation using BaseScreen from :core
```
Feel free to modify and expand upon the generated code to fit the specific needs of your feature. For example, you might want to add additional layers such as use cases in the domain layer or data sources in the data layer.

## Project structure
The project is organized into several key modules to promote a clean architecture and separation of concerns:
- `app`: The main application module that serves as the entry point for the app.
- `core`: A shared module containing common utilities, base classes such as `PatternViewModel`, and components used across multiple features.
- `buildSrc`: A module for managing dependencies and versions in a centralized manner, containing Gradle custom tasks including the feature generation task.
- `feature`: A directory containing all feature modules generated using the custom Gradle task.
- `baselineprofile`: A module for baseline profile configurations to optimize app performance.
- `navigation`: A module dedicated to handling navigation logic and definitions across the app.

## Technologies used
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)
![Jetpack Navigation](https://img.shields.io/badge/Jetpack%20Navigation-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Hilt](https://img.shields.io/badge/Hilt-E040FB?style=for-the-badge&logo=google&logoColor=white)
![MVI](https://img.shields.io/badge/Architecture-MVI-FF9800?style=for-the-badge)
![Modular](https://img.shields.io/badge/Structure-Modular-blueviolet?style=for-the-badge)
![Gradle](https://img.shields.io/badge/Gradle_Tasks-02303A?style=for-the-badge&logo=gradle&logoColor=white)