pluginManagement {
    repositories {
        google()
        mavenCentral()
        //noinspection JcenterRepositoryObsolete
        jcenter()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        //noinspection JcenterRepositoryObsolete
        jcenter()
        mavenCentral()
    }
}

rootProject.name = "Учедус"
include(":app")
 