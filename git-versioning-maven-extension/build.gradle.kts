plugins {
    id("git-versioning.java-conventions")
}

val itMavenDistributions = configurations.create("itMavenDistributions") {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    api(project(":git-versioning-core"))
    implementation(libs.jackson.dataformat.xml)
    implementation(libs.decentxml)

    compileOnly(libs.maven.plugin.api)
    compileOnly(libs.maven.core)
    compileOnly(libs.maven.plugin.annotations)
    annotationProcessor(libs.sisu.inject)
    compileOnly(libs.sisu.inject)

    itMavenDistributions("org.apache.maven:apache-maven:${libs.versions.maven.get()}:bin@zip")

    testImplementation(libs.maven.plugin.api)
    testImplementation(libs.maven.core)
    testImplementation(libs.maven.artifact)
    testImplementation(libs.maven.verifier)
    testRuntimeOnly(libs.slf4j.simple)
}

tasks.processResources {
    filesMatching("META-INF/maven/plugin.xml") {
        filter<org.apache.tools.ant.filters.ReplaceTokens>(
            "tokens" to mapOf(
                "groupId" to project.group.toString(),
                "artifactId" to project.name,
                "version" to project.version.toString(),
            )
        )
    }
    filesMatching("mavenBuild.properties") {
        expand(
            "project" to mapOf(
                "groupId" to project.group.toString(),
                "artifactId" to project.name,
                "version" to project.version.toString(),
            )
        )
    }
}

tasks.test {
    exclude("**/*IT.class")
    systemProperty("maven.home", System.getProperty("maven.home") ?: System.getenv("MAVEN_HOME") ?: "")
}

val unpackItMavenDistributions = tasks.register<Sync>("unpackItMavenDistributions") {
    from({ itMavenDistributions.files.map { zipTree(it) } })
    into(layout.buildDirectory.dir("it-maven-distributions"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

val integrationTest = tasks.register<Test>("integrationTest") {
    description = "Runs the *IT classes against the published extension"
    group = "verification"
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    include("**/*IT.class")
    dependsOn(unpackItMavenDistributions)
    dependsOn(":git-versioning-core:publishToMavenLocal", "publishToMavenLocal")
    systemProperty("it.maven.distributions.dir", layout.buildDirectory.dir("it-maven-distributions").get().asFile.absolutePath)
    systemProperty("maven.home", System.getProperty("maven.home") ?: System.getenv("MAVEN_HOME") ?: "")
    maxParallelForks = 1
    testLogging { events("failed", "passed"); showStandardStreams = false }
}

tasks.compileJava {
    options.compilerArgs.addAll(listOf("-processor", "org.eclipse.sisu.space.SisuIndexAPT6"))
}
