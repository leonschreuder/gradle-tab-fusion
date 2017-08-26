Gradle-Tab-Fusion
----------------------------------------

For commandline autocompletion in gradle I've been building [this
plugin](https://github.com/meonlol/gradle-tab-completion), and later I found
gradle recommends [this one](https://github.com/gradle/gradle-completion).
Both work verry nice an all, but since I work in loads of repos
simultaniously, the caching of a few seconds that is required in each one is
super anoying. Plus, when you modify build scripts often (which I do) you have
to rebuild the cache constantly.

So this now is a plugin that generates the cache file from gradle itself. When
you apply it, every time you run any build task the cache gets refreshed, and
there is no need to wait for cache be rebuilt unless it's the first time you
try to build the project.
