finagle-circuit
===========
<a href="https://travis-ci.org/daviddenton/finagle-circuit" target="_top">
<img src="https://travis-ci.org/daviddenton/finagle-circuit.svg?branch=master"/></a> 
<a href="https://coveralls.io/r/daviddenton/finagle-circuit?branch=master" target="_top"><img src="https://coveralls.io/repos/daviddenton/finagle-circuit/badge.svg?branch=master"/></a> 
<a href="https://bintray.com/daviddenton/maven/finagle-circuit/_latestVersion" target="_top"><img src="https://api.bintray.com/packages/daviddenton/maven/finagle-circuit/images/download.svg"/></a> 
<a href="https://bintray.com/daviddenton/maven/finagle-circuit/view?source=watch" target="_top"><img src="https://www.bintray.com/docs/images/bintray_badge_color.png"/></a> 

Finagle-circuit is a bolt-on HTTP circuit-breaking library for the [Finagle](http://twitter.github.io/finagle/) RPC framework from Twitter. 
It provides a more a granular circuit-breaking ability and includes a HTTP Filter which can be used with Finagle servers and clients.

###See it
See the [example code](https://github.com/daviddenton/finagle-circuit/tree/master/src/test/scala/examples).

###Get it
Add the following lines to ```build.sbt```. Note that this library doesn't depend on a particular version of Finagle,
it has been built and tested with the version below:

```scala
resolvers += "JCenter" at "https://jcenter.bintray.com"
libraryDependencies += "com.twitter" %% "finagle-http" % "6.26.0"
libraryDependencies += "io.github.daviddenton" %% "finagle-circuit" % "X.X.X"
```
