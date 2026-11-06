plugins {
    id("git-versioning.java-conventions")
    `java-gradle-plugin`
}

dependencies {
    implementation(project(":git-versioning-core"))
}
