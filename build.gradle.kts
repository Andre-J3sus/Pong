import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
}

group = "me.andre"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    flatDir { dirs("libs") }
}

dependencies { // Adicionar esta secção “dependencies” caso não exista
    implementation("pt.isel:CanvasLib-jvm:1.0.0") // Adicionar esta linha
}


tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "13"
}