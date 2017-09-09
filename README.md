Gradle-Tab-Fusion
----------------------------------------
[gradle-tab-fusion in the gradle plugin portal](https://plugins.gradle.org/plugin/de.leonmoll.gradle.gradle-tab-fusion)

For commandline autocompletion in gradle there
are several options - for example [my initial
completer](https://github.com/meonlol/gradle-tab-completion) or [gradle's
completer](https://github.com/gradle/gradle-completion) - but most are reliant
on custom generated caches. This caching is slow and has to be done way to
often, which I find terribly annoying.

**Introducing: Gradle-Tab-Fusion**

This is is not just a bash completer, it is also a plugin for Gradle itself.
All that tedious waiting of the cache to be built is now delegated to gradle
itself, which it will do every time you run any build task.

## Installation
For every project you want completion for, put this in it's `build.gradle` file:

    buildscript {
      repositories {
        maven {
          url "https://plugins.gradle.org/m2/"
        }
      }
      dependencies {
        classpath "gradle.plugin.de.leonmoll.gradle:gradle-tab-fusion:0.2.0"
      }
    }

    apply plugin: 'java'    // Important. The task is prepended on the assemble task, which must be available first.
    apply plugin: "de.leonmoll.gradle.gradle-tab-fusion"

And now all you've got to do is run the install tasks `installTabCompletion`
once, and you can complete happily in every project that has the plugin
applied.
