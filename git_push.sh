git add gradle/libs.versions.toml app/build.gradle.kts
git commit -m "build: bump Room to 2.7.2 and add Timber dependencies"

git add app/src/main/java/com/anishkun/hidetext/HideTextApp.kt
git commit -m "chore: initialize Timber in application class"

git add app/src/main/java/com/anishkun/hidetext/domain/model/Message.kt
git commit -m "feat(domain): define Message domain model"

git add app/src/main/java/com/anishkun/hidetext/domain/repository/ChatRepository.kt
git commit -m "feat(domain): create ChatRepository interface contract"

git add app/src/main/java/com/anishkun/hidetext/data/local/entity/MessageEntity.kt
git commit -m "feat(data): implement MessageEntity for local storage"

git add app/src/main/java/com/anishkun/hidetext/data/local/dao/MessageDao.kt
git commit -m "feat(data): implement Room MessageDao"

git add app/src/main/java/com/anishkun/hidetext/data/local/ChatDatabase.kt
git commit -m "feat(data): setup Room ChatDatabase"

git add app/src/main/java/com/anishkun/hidetext/data/remote/websocket/ChatWebSocketClient.kt
git commit -m "feat(data): implement OkHttp ChatWebSocketClient for real-time traffic"

git add app/src/main/java/com/anishkun/hidetext/data/repository/ChatRepositoryImpl.kt
git commit -m "feat(data): implement ChatRepositoryImpl to coordinate Room and WebSockets"

git add app/src/main/java/com/anishkun/hidetext/di/AppModule.kt
git commit -m "feat(di): provide application CoroutineScope in AppModule"

git add app/src/main/java/com/anishkun/hidetext/di/DatabaseModule.kt
git commit -m "feat(di): setup DatabaseModule for Room injection"

git add app/src/main/java/com/anishkun/hidetext/di/NetworkModule.kt
git commit -m "feat(di): setup NetworkModule for OkHttpClient and WebSocket injection"

git add app/src/main/java/com/anishkun/hidetext/di/RepositoryModule.kt
git commit -m "feat(di): bind ChatRepositoryImpl in RepositoryModule"

git add app/src/main/java/com/anishkun/hidetext/presentation/main/AppMode.kt
git commit -m "feat(presentation): define AppMode enum for gatekeeper state"

git add app/src/main/java/com/anishkun/hidetext/presentation/main/MainState.kt
git commit -m "feat(presentation): define MainState data class"

git add app/src/main/java/com/anishkun/hidetext/presentation/main/MainViewModel.kt
git commit -m "feat(presentation): implement MainViewModel with PIN verification logic"

git add app/src/main/java/com/anishkun/hidetext/presentation/decoy/DecoyCalculatorScreen.kt
git commit -m "feat(presentation): implement DecoyCalculatorScreen UI"

git add app/src/main/java/com/anishkun/hidetext/presentation/main/GatekeeperScreen.kt
git commit -m "feat(presentation): implement GatekeeperScreen for UI transitions"

git add app/src/main/java/com/anishkun/hidetext/MainActivity.kt
git commit -m "feat(presentation): set GatekeeperScreen as MainActivity content"

git push -u origin main
