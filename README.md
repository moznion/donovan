donovan [![Build Status](https://travis-ci.org/moznion/donovan.svg)](https://travis-ci.org/moznion/donovan)
==

Description
--

Deadly simple Java 8 web application framework for mocking and testing.

This framework aims to bootstrap a web application by single java file.
Donovan uses embedded Jetty or Tomcat to realize this purpose.

This framework is inspired by [avans](https://github.com/tokuhirom/avans).

Synopsis
---

### With Embedded Jetty

#### App.java

```java
@Slf4j
public class App {
    public static void main(String[] args) throws Exception {
        try (DonovanJetty dj = new DonovanJetty()) {
            dj.get("/", (c) -> {
                return c.renderJSON(new BasicAPIResponse(200, "hello!"));
            });

            dj.start();

            String url = dj.getURL();
            log.info(url);

            Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
            Mech2Result result = mech.get("/").execute();

            log.debug("{}", result.getResponse().getStatusLine().getStatusCode()); // <= 200
            log.debug("{}", result.getResponseBodyAsString()); // <= {"code":200,"messages":["hello!"]}
        }
    }
}
```

#### pom.xml

```xml
...
<dependencies>
    ...
    <dependency>
        <groupId>net.moznion</groupId>
        <artifactId>donovan</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>1.7.12</version>
    </dependency>
    <dependency>
        <groupId>me.geso</groupId>
        <artifactId>mech2</artifactId>
        <version>0.3.0</version>
    </dependency>
    <dependency>
        <groupId>org.eclipse.jetty</groupId>
        <artifactId>jetty-server</artifactId>
        <version>9.2.10.v20150310</version>
    </dependency>
</dependencies>
```

You must specify `jetty-server` if you want to use donovan with jetty.

### With Embedded Tomcat

#### App.java

```java
@Slf4j
public class App {
    public static void main(String[] args) throws Exception {
        try (DonovanTomcat dt = new DonovanTomcat()) {
            dt.get("/", (c) -> {
                return c.renderJSON(new BasicAPIResponse());
            });

            dt.start();

            String url = dt.getURL();
            log.info(url);

            Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
            Mech2Result result = mech.get("/").execute();

            log.debug("{}", result.getResponse().getStatusLine().getStatusCode()); // <= 200
            log.debug("{}", result.getResponseBodyAsString()); // <= {"code":200,"messages":["hello!"]}
        }
    }
}
```

#### pom.xml

```xml
...
<dependencies>
    ...
    <dependency>
        <groupId>net.moznion</groupId>
        <artifactId>donovan</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>1.7.12</version>
    </dependency>
    <dependency>
        <groupId>me.geso</groupId>
        <artifactId>mech2</artifactId>
        <version>0.3.0</version>
    </dependency>
    <dependency>
        <groupId>org.apache.tomcat.embed</groupId>
        <artifactId>tomcat-embed-core</artifactId>
        <version>8.0.21</version>
    </dependency>
        <dependency>
        <groupId>org.apache.tomcat</groupId>
        <artifactId>tomcat-catalina</artifactId>
        <version>8.0.21</version>
    </dependency>
</dependencies>
```

You must specify `tomcat-embed-core` and `tomcat-catalina` if you want to use donovan with tomcat.

### Run a web app

```
$ mvn compile
$ mvn exec:java -Dexec.mainClass="com.example.App"
```

FAQ
--

Q. Can I use this framework in production?  
A. You'd better not do it.

Author
--

moznion (<moznion@gmail.com>)

License
--

```
The MIT License (MIT)
Copyright © 2015 moznion, http://moznion.net/ <moznion@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the “Software”), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```

