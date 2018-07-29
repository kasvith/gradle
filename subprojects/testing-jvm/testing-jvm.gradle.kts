import org.gradle.gradlebuild.unittestandcompile.ModuleType

/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
plugins {
    // TODO: re-enable if we are ready to do breaking changes, because this subproject includes classes migrated from the "plugins" subproject
    // id 'gradlebuild.strict-compile'
    // id 'gradlebuild.classycle'
}

dependencies {
    compile(project(":core"))
    compile(project(":platformJvm"))
    compile(project(":languageJava"))
    compile(project(":testingBase"))

    implementation(library("asm"))
    implementation(library("commons_io"))
    implementation(library("junit"))
    implementation(library("testng"))
    implementation(library("bsh"))

    testCompile ("com.google.inject:guice:2.0") {
        because("This is for TestNG")
    }

    integTestRuntime(project(":testingJunitPlatform"))
}

gradlebuildJava {
    moduleType = ModuleType.WORKER
}

tasks.withType<Test>().named("test").configure {
    exclude("org/gradle/api/internal/tasks/testing/junit/ATestClass*.*")
    exclude("org/gradle/api/internal/tasks/testing/junit/ABroken*TestClass*.*")
}

testFixtures {
    from(":core")
    from(":testingBase")
    from(":diagnostics")
    from(":messaging")
    from(":baseServices")
}
