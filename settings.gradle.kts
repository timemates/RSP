enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://maven.timemates.org")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.y9vad9.com")
        maven("https://maven.timemates.org")
    }
}

rootProject.name = "rrpc"

includeBuild("build-conventions")

include(
    ":common:core",
    ":common:schema",
)

include(
    ":server:core",
    ":server:schema",
)

include(
    ":client:core",
    ":client:schema",
)

include(
    ":generator:core",
    ":generator:kotlin",
)

include(":integration-tests")

include(
    //":tools:rrpcurl",
    ":tooling:generator-cli",
    ":tooling:generator-gradle-plugin",
)

include(
    ":internal:dynamic-serialization"
)
