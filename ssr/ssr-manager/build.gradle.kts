import org.gradle.api.internal.HasConvention
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.3.11"
}

group = "hoshino9.org"
version = "1.0-SNAPSHOT"

val SourceSet.kotlin get() = (this as HasConvention).convention.getPlugin(KotlinSourceSet::class).kotlin

sourceSets {
	val main by getting {
		kotlin.srcDir("src")
	}
}

repositories {
	jcenter()
}

dependencies {
	compile(kotlin("stdlib-jdk8"))
	compile("org.json:json:20180813")
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "1.8"
}

val jar : Jar by tasks

jar.apply {
	manifest {
		attributes(mapOf("Main-Class" to "MainKt"))
	}

	from(configurations.compile.get().map { if (it.isDirectory) it else zipTree(it) })
}