# This is a fork

The original creator of PhantomJS driver [Ivan De Marino](https://github.com/detro) cannot support the project anymore.

That's why we forked the original repo to [github.com/codeborne/ghostdriver](https://github.com/codeborne/ghostdriver).

Any help is still needed, especially pull requests.

---

# Ghost Driver

Ghost Driver is a pure JavaScript implementation of the
[WebDriver Wire Protocol](http://code.google.com/p/selenium/wiki/JsonWireProtocol)
for [PhantomJS](http://phantomjs.org/).
It's a Remote WebDriver that uses PhantomJS as back-end.

**GhostDriver is designed to be integral part of PhantomJS itself, but it's developed in isolation and progress is tracked
by this Repository.**

* Current _GhostDriver_ stable version:
see [releases](https://github.com/detro/ghostdriver/releases)
* Current _PhantomJS-integrated_ version is `"1.1.0"`:
contained in PhantomJS `"1.9.7"`
* Current _PhantomJSDriver Java bindings_ stable version: see
[Maven](https://oss.sonatype.org/index.html#nexus-search;quick~phantomjsdriver)

For more info, please take a look at the [changelog](https://github.com/detro/ghostdriver/blob/master/CHANGELOG.md).

The project was created and is lead by [Ivan De Marino](https://github.com/detro).

Current fork is supported by [Andrei Solntsev](https://github.com/asolntsev).

IRC channel: [#phantomjs-ghostdriver](http://webchat.freenode.net/?channels=%23phantomjs-ghostdriver).

## Setup

* Download latest stable PhantomJS from [here](http://phantomjs.org/download.html)
* Selenium version `">= 3.3.1`"

**THAT'S IT!!** Because of latest stable GhostDriver being embedded in PhantomJS,
you shouldn't need anything else to get started.

## Register GhostDriver with a Selenium Grid hub

1. Launch the grid server, which listens on 4444 by default: `java -jar /path/to/selenium-server-standalone-<SELENIUM VERSION>.jar -role hub`
2. Register with the hub: `phantomjs --webdriver=8080 --webdriver-selenium-grid-hub=http://127.0.0.1:4444`
3. Now you can use your normal webdriver client with `http://127.0.0.1:4444` and just request `browserName: phantomjs`

## (Java) Bindings

This project provides WebDriver bindings for Java under the name _PhantomJSDriver_.
[Here is the JavaDoc for this fork](https://cdn.rawgit.com/codeborne/ghostdriver/master/binding/java/docs/javadoc/index.html).

Bindings for other languages (C#, Python, Ruby, ...) are developed and maintained
under the same name within the [Selenium project](http://docs.seleniumhq.org/docs/) itself.

### Include Java Bindings in your Maven project

For versions >= 1.2.1, add the following to your `pom.xml`:

```xml
<dependency>
    <groupId>com.codeborne</groupId>
    <artifactId>phantomjsdriver</artifactId>
    <version>1.4.4</version>
</dependency>
```

### Include Java Bindings in your Gradle project

Just add the following to your `build.gradle`:

```gradle
dependencies {
    ...
    testCompile "com.codeborne:phantomjsdriver:LATEST_VERSION_HERE"
    ...
}
```

### Search Maven
For other options on how to include the PhantomJSDriver dependency into your project, click [here](http://search.maven.org/#search%7Cga%7C1%7Ccom.codeborne.phantomjsdriver).

### Alternative: how to use it via `RemoteWebDriver`

Launching PhantomJS in Remote WebDriver mode it's simple:
```bash
$ phantomjs --webdriver=PORT
```
Once started, you can use any `RemoteWebDriver` implementation to send commands to it. I advice to take a look to the
`/test` directory for examples.

## F.A.Q.

### What extra WebDriver `capabilities` GhostDriver offers?
* GhostDriver extra Capabilities
    * `phantomjs.page.settings.SETTING = VALUE` - Configure `page.settings`
    on PhantomJS internal page objects (_windows_ in WebDriver context)
    (see [reference](http://phantomjs.org/api/webpage/property/settings.html))
    * `phantomjs.page.customHeaders.HEADER = VALUE` - Add extra HTTP Headers
    when loading a URL
    (see [reference](http://phantomjs.org/api/webpage/property/custom-headers.html))
* PhantomJSDriver (Java-binding) Capabilities
    * `phantomjs.binary.path` - Specify path to PhantomJS executable to use
    * `phantomjs.ghostdriver.path` - Specify path to GhostDriver `main/src.js`
    script to use; allows to use a different version of GhostDriver then the one
    embed in PhantomJS
    * `phantomjs.cli.args` - Specify command line arguments to pass to the
    PhantomJS executable
    * `phantomjs.ghostdriver.cli.args` - Specify command line argument to pass to
    GhostDriver (works only in tandem with `phantomjs.ghostdriver.path`)

## Want to help? Read on!

GhostDriver pushed the evolution of PhantomJS from the start. All the features required by PhantomJS to fit GhostDriver were designed to still feel "consistent" and "at home" with PhantomJS alone.

To drive that effort, I worked on a [PhantomJS fork](https://github.com/detro/phantomjs-ghostdriver), and then
pushed changes to PhantomJS master once agreed with the rest of the team on the changes.

If you are planning to contribute, that is the PhantomJS you should use.

### Run validation tests

Here I show how to clone this repo and kick start the (Java) tests. You need
[Java SDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
to run them (Java 8 is required).

1. `git clone https://github.com/detro/ghostdriver.git`
2. Configure `phantomjs_exec_path` inside `ghostdriver/test/config.ini` to point at the build of PhantomJS you just did
3. `cd ghostdriver/test/java; ./gradlew test`

#### Alternative: Run GhostDriver yourself and launch tests against that instance

1. `phantomjs --webdriver=PORT`
2. Configure `driver` inside `ghostdriver/test/config.ini` to point at the URL `http://localhost:PORT`
3. `cd ghostdriver/test/java; ./gradlew test`

### Project Directory Structure

Here follows the output of the `tree -hd -L 3` command, trimmed of files and "build directories":

```bash
.
├── [ 102]  binding
│   └── [ 510]  java
│       ├── [ 204]  build
│       ├── [ 136]  gradle
│       ├── [ 884]  jars            <--- JARs containing Binding, related Source and related JavaDoc
│       └── [ 102]  src             <--- Java Binding Source
├── [ 442]  src                     <--- GhostDriver JavaScript core source
│   ├── [ 306]  request_handlers    <--- JavaScript "classes/functions" that handle HTTP Requests
│   └── [ 204]  third_party         <--- Third party/utility code
│       └── [2.0K]  webdriver-atoms <--- WebDriver Atoms, automatically imported from the Selenium project
├── [ 204]  test
│   ├── [ 476]  java                <--- Java Tests
│   │   ├── [ 136]  gradle
│   │   ├── [ 136]  out
│   │   └── [ 102]  src
│   ├── [ 238]  python              <--- Python Tests
│   │   └── [ 102]  utils
│   └── [ 340]  testcase-issue_240
└── [ 238]  tools                   <--- Tools (import/export)
    └── [ 136]  atoms_build_dir

20 directories
```

### WebDriver Atoms

Being GhostDriver a WebDriver implementation, it embeds the standard/default WebDriver Atoms to operate inside open
webpages. In the specific, the Atoms cover scenarios where the "native" PhantomJS `require('webpage')` don't stretch.

Documentation about how those work can be found [here](http://code.google.com/p/selenium/wiki/AutomationAtoms)
and [here](http://www.aosabook.org/en/selenium.html).

How are those Atoms making their way into GhostDriver? If you look inside the `/tools` directory you can find a bash
script: `/tools/import_atoms.sh`. That script accepts the path to a Selenium local repo, runs the
[CrazyFunBuild](http://code.google.com/p/selenium/wiki/CrazyFunBuild) to produce the compressed/minified Atoms,
grabs those and copies them over to the `/src/third_party/webdriver-atoms` directory.

The Atoms original source lives inside the Selenium repo in the subtree of `/javascript`. To understand how the build
works, you need to spend a bit of time reading about
[CrazyFunBuild](http://code.google.com/p/selenium/wiki/CrazyFunBuild): worth your time if you want to contribute to
GhostDriver (or any WebDriver, as a matter of fact).

One thing it's important to mention, is that CrazyFunBuild relies on the content of `build.desc` file to understand
what and how to build it. Those files define what exactly is built and what it depends on. In the case of the Atoms,
the word "build" means "run Google Closure Compiler over a set of files and compress functions into Atoms".
The definition of the Atoms that GhostDriver uses lives at `/tools/atoms_build_dir/build.desc`.

Let's take this small portion of our `build.desc`:
```
js_deps(name = "deps",
  srcs = "*.js",
  deps = ["//javascript/atoms:deps",
          "//javascript/webdriver/atoms:deps"])

js_fragment(name = "get_element_from_cache",
  module = "bot.inject.cache",
  function = "bot.inject.cache.getElement",
  deps = [ "//javascript/atoms:deps" ])

js_deps(name = "build_atoms",
  deps = [
    ...
    "//javascript/webdriver/atoms:execute_script",
    ...
  ]
```
The first part (`js_deps(name = "deps"...`) declares what are the dependency of this `build.desc`: with that CrazyFunBuild knows
what to build before fulfilling our build.

The second part (`js_fragment(...`) defines an Atom: the `get_element_from_cache` is going to be the name of
an Atom to build; it can be found in the module `bot.inject.cache` and is realised by the function named
`bot.inject.cache.getElement`.

The third part (`js_deps(name = "build_atoms"...`) is a list of the Atoms (either defined by something like the second
part or in one of the files we declared as dependency) that we want to build.

If you reached this stage in understanding the Atoms, you are ready to go further by yourself.

### Contributions and/or Bug Report

You can contribute by testing GhostDriver, reporting bugs and issues, or submitting Pull Requests.
Any **help is welcome**, but bear in mind the following base principles:

* Issue reporting requires a reproducible example, otherwise will most probably be **closed without warning**
* Squash your commits by theme: I prefer a clean, readable log
* Maintain consistency with the code-style you are surrounded by
* If you are going to make a big, substantial change, let's discuss it first
* I **HATE** CoffeeScript: assume I'm going to laugh off any "contribution" that contains such _aberrational crap_!
* Open Source is NOT a democracy (and I mean it!)

## License
GhostDriver is distributed under [BSD License](http://www.opensource.org/licenses/BSD-2-Clause).

## Release names
See [here](http://en.wikipedia.org/wiki/List_of_ghosts).
