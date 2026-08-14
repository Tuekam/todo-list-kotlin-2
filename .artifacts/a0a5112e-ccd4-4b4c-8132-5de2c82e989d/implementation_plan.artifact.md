# Plan d'implémentation : Todo List KMP (Clean Architecture + Decompose)

Reconstruction propre et atomique du projet Todo List en utilisant Kotlin Multiplatform, Decompose pour la navigation, Koin pour l'injection de dépendances et Ktor pour le backend Next.js.

## User Review Required

> [!IMPORTANT]
> **Structure des Modules** : Nous allons utiliser `:app` comme module "Shared" principal qui assemble tous les autres. Les modules `:core`, `:data`, `:domain` et `:presentation` seront des modules KMP atomiques.
> **Navigation** : Utilisation de **Decompose** pour une navigation typée et indépendante de la plateforme.

## Proposed Changes

### 1. Configuration Globale (Dépendances)
Mise à jour du catalogue de versions pour centraliser tous les outils nécessaires.

#### [MODIFY] [libs.versions.toml](file:///C:/Users/PROMOPlus/AndroidStudioProjects/todo_list_kotlin/gradle/libs.versions.toml)
- Ajout de Decompose (3.5.0)
- Ajout de Ktor (3.5.2)
- Ajout de Koin (4.2.2)
- Ajout de Kotlinx Serialization & Coroutines

### 2. Module `:core` (Modèles Atomiques)
Contient les objets de données purs (POJO) utilisés par toutes les couches.

#### [MODIFY] [Task.kt](file:///C:/Users/PROMOPlus/AndroidStudioProjects/todo_list_kotlin/core/src/commonMain/kotlin/com/example/core/models/Task.kt)
- Modèle de donnée `Task` avec `@Serializable`.

### 3. Module `:domain` (Contrats Métier)
Définit *ce que* l'application fait via des interfaces et des UseCases.

#### [MODIFY] [TaskRepository.kt](file:///C:/Users/PROMOPlus/AndroidStudioProjects/todo_list_kotlin/domain/src/commonMain/kotlin/com/example/domain/repositories/TaskRepository.kt)
- Interface de dépôt pour les opérations CRUD.
#### [NEW] [GetTasksUseCase.kt](file:///C:/Users/PROMOPlus/AndroidStudioProjects/todo_list_kotlin/domain/src/commonMain/kotlin/com/example/domain/usecases/GetTasksUseCase.kt)
- UseCase atomique pour récupérer les tâches.

### 4. Module `:data` (Infrastructure & API)
Implémente les contrats du domaine en communiquant avec le backend Next.js.

#### [NEW] [TaskApi.kt](file:///C:/Users/PROMOPlus/AndroidStudioProjects/todo_list_kotlin/data/src/commonMain/kotlin/com/example/data/api/TaskApi.kt)
- Client Ktor configuré pour `http://192.168.87.21:3000`.
#### [MODIFY] [TaskRepositoryImpl.kt](file:///C:/Users/PROMOPlus/AndroidStudioProjects/todo_list_kotlin/data/src/commonMain/kotlin/com/example/data/repositories/TaskRepositoryImpl.kt)
- Implémentation utilisant le client Ktor.

### 5. Module `:presentation` (Composants Decompose)
Gère la logique de navigation et l'état de l'interface utilisateur de manière multiplateforme.

#### [NEW] [RootComponent.kt](file:///C:/Users/PROMOPlus/AndroidStudioProjects/todo_list_kotlin/presentation/src/commonMain/kotlin/com/example/presentation/root/RootComponent.kt)
- Composant racine Decompose gérant la Stack de navigation.
#### [NEW] [TaskListComponent.kt](file:///C:/Users/PROMOPlus/AndroidStudioProjects/todo_list_kotlin/presentation/src/commonMain/kotlin/com/example/presentation/screens/list/TaskListComponent.kt)
- Logique de l'écran de liste.

### 6. Module `:app` (Assemblage & DI)
Le point d'entrée "Shared" qui expose les composants aux plateformes Android/iOS.

#### [NEW] [AppModule.kt](file:///C:/Users/PROMOPlus/AndroidStudioProjects/todo_list_kotlin/app/src/commonMain/kotlin/com/example/app/di/AppModule.kt)
- Configuration Koin assemblant tous les modules.

### 7. Module `:androidApp` (Interface Android)
Affiche l'UI en utilisant Jetpack Compose et Decompose.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/PROMOPlus/AndroidStudioProjects/todo_list_kotlin/androidApp/src/main/kotlin/com/example/todo_list_kotlin/MainActivity.kt)
- Initialisation de Decompose et affichage du RootContent.

## Verification Plan

### Automated Tests
- `./gradlew :domain:commonTest` : Vérification de la logique métier.
- `./gradlew :data:commonTest` : Tests (mockés) des appels API.

### Manual Verification
- Lancement sur téléphone physique pour tester la connexion au backend Next.js sur `192.168.87.21:3000`.
- Vérification de la fluidité de la navigation Decompose (Stack).
