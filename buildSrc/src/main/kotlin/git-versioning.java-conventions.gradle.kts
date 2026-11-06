plugins {
    `java-library`
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    testImplementation(platform(libs.findLibrary("junit-bom").get()))
    testImplementation(libs.findLibrary("junit-jupiter").get())
    testImplementation(libs.findLibrary("assertj-core").get())
    testRuntimeOnly(libs.findLibrary("junit-platform-launcher").get())
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = 17
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name = project.name
            description = "Git versioning: ${project.name}"
            url = "https://github.com/mrozanc/maven-git-versioning-extension"
            scm {
                url = "https://github.com/mrozanc/maven-git-versioning-extension.git"
                connection = "scm:git:https://github.com/mrozanc/maven-git-versioning-extension.git"
            }
            licenses {
                license {
                    name = "The MIT License (MIT)"
                    url = "https://opensource.org/licenses/MIT"
                    distribution = "repo"
                }
            }
            developers {
                developer {
                    name = "Marc ROZANC"
                    url = "https://github.com/mrozanc"
                    roles.add("developer")
                }
                developer {
                    name = "Bengt Brodersen"
                    url = "https://github.com/qoomon"
                    roles.add("original project creator")
                }
            }
        }
    }
}

// Release: ./gradlew publish -Prelease -PreleaseRepositoryUrl=... (credentials via releaseRepositoryUsername/Password)
// Signing: -Psigning.gnupg.keyName=... with useGpgCmd, or in-memory keys through ORG_GRADLE_PROJECT_signingKey
if (providers.gradleProperty("release").isPresent) {
    signing {
        useGpgCmd()
        sign(publishing.publications)
    }
    tasks.withType<AbstractPublishToMaven>().configureEach {
        doFirst {
            check(!project.version.toString().endsWith("SNAPSHOT")) { "Cannot release a SNAPSHOT version" }
        }
    }
    providers.gradleProperty("releaseRepositoryUrl").orNull?.let { repoUrl ->
        publishing.repositories.maven {
            name = "release"
            url = uri(repoUrl)
            credentials {
                username = providers.gradleProperty("releaseRepositoryUsername").orNull
                password = providers.gradleProperty("releaseRepositoryPassword").orNull
            }
        }
    }
}
