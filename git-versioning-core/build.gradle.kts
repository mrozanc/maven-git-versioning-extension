plugins {
    id("git-versioning.java-conventions")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

dependencies {
    api(libs.jgit)
    compileOnly(libs.jspecify)
    testImplementation(libs.maven.artifact)
}
