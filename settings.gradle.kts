pluginManagement {
    repositories {
        maven {
            url =uri("https://maven.aliyun.com/repository/google")
        }
        maven {
            url =uri("https://maven.aliyun.com/repository/central")
        }
        maven {
            url =uri("https://maven.aliyun.com/repository/gradle-plugin")
        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url =uri("https://maven.aliyun.com/repository/google")
        }
        maven {
            url =uri("https://maven.aliyun.com/repository/central")
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "Herat_beat"
include(":app")
