finagle-circuit
===========
<a href="https://travis-ci.org/daviddenton/finagle-circuit" target="_top">
<img src="https://travis-ci.org/daviddenton/finagle-circuit.svg"/></a> 
<a href="https://coveralls.io/r/daviddenton/finagle-circuit" target="_top"><img src="https://coveralls.io/repos/daviddenton/finagle-circuit/badge.svg"/></a> 
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
libraryDependencies += "com.twitter" %% "finagle-httpx" % "6.29.0"
libraryDependencies += "io.github.daviddenton" %% "finagle-circuit" % "X.X.X"
```

###Migration notes

####v3.0.0
- Breaking change: simple repackage of all classes into ```io.github.finaglecircuit```
- Upgrade Akka-actor dependency

####v2.0.0
- Upgrading to v6.29.0 of Finagle-httpx. Finagle-http has been removed as of this release, so have migrated the API 
to use this instead.
    - References to ```HttpRequest/Response``` Netty classes are now ```Request/Response``` instead
    - References to ```HttpResponseStatus``` changed to ```Status```
    - References to ππ```HttpMethod.GET/POST/...``` changd to ```Method.Get/Post/...```
    - References to ```Http.XXX()``` will now use ```Http.XXX()``` instead

####v1.1.0
- Release to be used for versions of Finagle upto 6.28.0