# kessenger-lib
Repo is simple util library containing types, traits and classes for [Kessenger](https://github.com/malyszaryczlowiek/Kessenger) 
project. It is written in both Scala 2.12 and Scala 3 versions, and is stored in 
[Maven Central Repository](https://search.maven.org/) so is easy accessible with 
build tools like sbt or Maven. 


Simply add following line to your build.sbt file:

```
libraryDependencies ++= "io.github.malyszaryczlowiek" %% "kessenger-lib" % "0.2.3"
```
or Maven one (example with scala 2.12):
```
<dependency>
  <groupId>io.github.malyszaryczlowiek</groupId>
  <artifactId>kessenger-lib_2.12</artifactId>
  <version>0.2.3</version>
</dependency>
```