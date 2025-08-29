import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.maven.publish)
}

group = "me.mynna.dotenv"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    sourceSets {
        jvmToolchain(17)

        jvm()

        js(IR) {
            nodejs {
                testTask {
                    useMocha {
                        timeout = "0"
                    }
                    environment("PROJECT_ROOT", rootDir.absolutePath)
                }
            }
            binaries.executable()
        }

        val commonMain by getting {
            dependencies {
                api(libs.kotlin.logging)
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.dotenv)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.junit.jupiter)
                implementation(libs.kotlin.test.junit5)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(npm("dotenv", "17.2.1"))
            }
        }
    }

}

tasks.withType<KotlinJsCompile>().configureEach {
    compilerOptions {
        target.set("es2015")
    }
}