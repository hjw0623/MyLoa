plugins {
    alias(libs.plugins.lostark.android.library)
    alias(libs.plugins.lostark.jvm.ktor)
}

android {
    namespace = "com.hjw0623.character.data"
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(projects.character.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.core)
}