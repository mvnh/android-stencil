# Android stencil
A modern, multi-module Android template featuring Jetpack Compose and custom Gradle tasks for automated feature generation

## About
This repository serves as a comprehensive template for building Android applications using the latest technologies and best practices. It is designed to help developers kickstart their projects with a solid foundation, featuring a modular architecture, Jetpack Compose for UI development, Hilt for dependency injection, and custom Gradle tasks to streamline the creation of new features.

## How to use
1. **Clone the repository**: Start by cloning this repository to your local machine.
2. **Generate new feature**: Use the provided Gradle task to generate a new feature module. You can do this by running the following command in your terminal:
   ```
   ./gradlew generateFeature -PfName=YourFeatureName
   ```
   Replace `YourFeatureName` with the desired name for your new feature. The feature will be created under the `feature` directory. You can also make use of additional parameter `--addToNavBar` to automatically integrate the new feature into the app's navigation bar.
3. **Sync the project**: After generating the new feature, sync your project with Gradle files (`File > Sync Project with Gradle Files` in Android Studio) to make Gradle aware of the new module.
4. **Customize your feature**: The generated feature module will include boilerplate code for a ViewModel (using MVI architecture), a Composable screen, dependency injection and navigation setup and will be 

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

## Technologies used
- **Kotlin**: The primary programming language for Android development.
- **Jetpack Compose**: A modern toolkit for building native Android UIs.
- **Jetpack Navigation**: A framework for navigating between different screens in the app.
- **Hilt**: A dependency injection library for Android that reduces boilerplate code.
- **MVI Architecture**: Model-View-Intent architecture pattern for managing UI state.
- **Modular Architecture**: Separation of features into distinct modules for better maintainability and scalability.
- **Custom Gradle Tasks**: Automated tasks to streamline feature generation and integration.