Gradle-Tab-Fusion
----------------------------------------

For commandline autocompletion in gradle there
are several options - for example [my initial
completer](https://github.com/meonlol/gradle-tab-completion) or [gradle's
completer](https://github.com/gradle/gradle-completion) - but most are reliant
on custom generated caches. This caching is slow and has to be done way to
often, which I found terribly annoying.

**Introducing: Gradle-Tab-Fusion**

This is is not just a bash completer, it is also a plugin for Gradle
itself. All the tedious waiting of the cache to be built is now delegated
to gradle itself, which it will do every time you run any build task.

## Installation
For every project you want completion for, put this in it's `build.gradle` file:

    plugins {
        id "de.leonmoll.gradle.gradle-tab-fusion" version "0.1"
    }

And now all you've got to do is run the install tasks `installTabCompletion`
once, and you can complete happily in every project that has the plugin
installed.
