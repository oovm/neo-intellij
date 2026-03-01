rootProject.name = "Neo Theme"

pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/gradle-plugin")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
        }
        gradlePluginPortal()
        mavenCentral()
    }
}
