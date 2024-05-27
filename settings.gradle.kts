pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        jcenter() // 添加jcenter仓库，以防某些依赖项在这里
    }
}

rootProject.name = "Deserialization_test"
include(":app")
