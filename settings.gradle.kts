pluginManagement {
    repositories {
        google()
        mavenCentral()
        //noinspection JcenterRepositoryObsolete
        jcenter()
        gradlePluginPortal()
        maven { url = uri("https://www.jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        //noinspection JcenterRepositoryObsolete
        jcenter()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io") }
    }
}

rootProject.name = "Учедус"
include(":app")
 