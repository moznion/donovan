donovan [![Build Status](https://travis-ci.org/moznion/donovan.svg)](https://travis-ci.org/moznion/donovan)
==

Deadly simple Java 8 web application framework for mocking and testing.

This is inspired by [avans](https://github.com/tokuhirom/avans).

Synopsis
---

### With Embedded Jetty

```java
try (DonovanJetty dj = new DonovanJetty()) {
    dj.get("/", (c) -> {
        return c.renderJSON(new BasicAPIResponse(200, "hello!"));
    });

    dj.start();

    String url = dj.getUrl();
    Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
    Mech2Result result = mech.get("/").execute();

    System.out.println(result.getResponse().getStatusLine().getStatusCode()); // <= 200
    System.out.println(result.getResponseBodyAsString()); // <= {"code":200,"messages":["hello!"]}
}
```

### With Embedded Tomcat

```java
try (DonovanTomcat dt = new DonovanTomcat()) {
    dt.get("/", (c) -> {
        return c.renderJSON(new BasicAPIResponse());
    });

    dt.start();

    String url = dt.getUrl();
    Mech2WithBase mech = new Mech2WithBase(Mech2.builder().build(), new URI(url));
    Mech2Result result = mech.get("/").execute();

    System.out.println(result.getResponse().getStatusLine().getStatusCode()); // <= 200
    System.out.println(result.getResponseBodyAsString()); // <= {"code":200,"messages":["hello!"]}
}
```

Description
--

TBD

Author
--

moznion (<moznion@gmail.com>)

License
--

```
The MIT License (MIT)
Copyright © 2014 moznion, http://moznion.net/ <moznion@gmail.com>

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

