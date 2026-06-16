git init -b main
git add gradle/ gradlew gradlew.bat settings.gradle.kts gradle.properties .gitignore
git commit -m "chore: project initialization with gradle wrapper"
git add build.gradle.kts app/build.gradle.kts gradle/libs.versions.toml
git commit -m "build: configure core dependencies for compose, room, hilt, and retrofit"
git add app/src/main/AndroidManifest.xml app/src/main/java/com/anishkun/hidetext/HideTextApp.kt
git commit -m "feat: setup application class and manifest networking permissions"
git add app/src/main/java/com/anishkun/hidetext/di/AppModule.kt
git commit -m "feat(di): implement hilt module for coroutine dispatchers"
git add .
git commit -m "feat(ui): setup presentation layer and clean architecture scaffolding"
git remote add origin https://github.com/anishkun/TextPrivate.git
git push -u origin main
