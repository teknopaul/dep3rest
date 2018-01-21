# Dependency free REST client

I have often been irked by the fact that when writing REST clients I've ended up with classpath issues.

* conflicting libraries on the classpath (dependency hell)
* stuff on the classpath that is hard to change.
* dependencies on server side DTOs.

One of the benefits of REST is meant to be the reduction of dependencies compared to solutions like EJB
that require Java code to be managed on server and client. With new JBoss version all notions of backwards compatibility are
out the window, even the minor versions of the JBoss server and all libraries needs to be the same on server and client.
That change makes EJB useless, there is no upgrade path for a cluster of JBoss servers so REST is the often the only viable solution.

This project provides some simple boilerplate code that has no dependencies other than the JVM. Code is JDK8 but trivially ported to other versions.
There are then some examples of how to introduce _jackson_ and _simple-json_ if that is desired, without having to match the classpath on the server side.

_java.net_ APIs are ugly, hence projects often use _netty_ or _http-client_, these dependencies often cause issues.
_http-client_ has been through non-backwards iterations. Core Java APIs are a lot more stable.

There is a maven `pom.xml` provided but its expected the sensible way to use this code is to copy paste [Dep3Rest.java](https://raw.githubusercontent.com/teknopaul/dep3rest/master/src/main/java/org/tp23/dep3rest/Dep3Rest.java).  Having a dependency
on this project defeats the main goal.

## JUnit tests

These are not unit tests, they require [nodetoy](http://github.com/teknopaul/nodetoy) running.

## Alternatives

Pick a version from RESTEasy or Spring. In theory the client version does not have to match the server, but due to use of annotations to
mark up DTOs for advanced JSON serialization its highly recommended at least to start with the same client as your server and to use the same
underlying JSON serialization mechanism jaxb or jackson 1 or 2 or whatever.

[RESTEasy](https://docs.jboss.org/resteasy/docs/2.3.7.Final//userguide/html/RESTEasy_Client_Framework.html)
[Spring RestTemplate](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html)

jax-rs 2.0 has a client API that theoretically will be more stable.

All of the above fall down on certain types of abstract JSON, a number, e.g. "5" is valid JSON. If you want a solution that makes no demands on
the server, or is the server JSON is out of your control and not written by Java code, I'd recommend json-simple.
Its not type-safe, neither is JSON.
