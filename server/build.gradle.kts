plugins {
    kotlin("jvm")
    application
}

group = "com.anishkun.server"
version = "1.0.0"

application {
    mainClass.set("com.anishkun.server.ApplicationKt")
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.logback)
}
