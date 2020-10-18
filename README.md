London User API
===============

My active GitHub account is linked to my current job, so this account has been created to keep them separate. 

## Brief

Using the language of your choice please build your own API which calls the API, and returns people who are listed as
either living in London, or whose current coordinates are within 50 miles of London. Push the answer to Github, and
send your test to ...

## Solution

### Running the application

The application can be run by using the green "play" button in `org.example.Application`, or by running the following
command in the terminal.

```
./gradlew clean bootRun
```

Unit tests can be run using the following command:

```
./gradlew clean test
```

Due to it being a multi-module gradle project, the test reports can be found in their respective module folders.

### Approach

I've followed something similar to the hexacore/ports & adapters approach of having a core module that is concerned 
solely with the business logic and can be easily tested. It also provides interfaces for the adapters to implement, so
they can be switched out if necessary and tested independently.

The core module contains the logic around filtering the response from the `/users` endpoint to only those found
within around 50 miles of London, and combining it with the response from the `/city/{city}/users` endpoint.

It depends on Jackson for deserialisation annotations (ideally, there'd be a separate class in the user-client-adapter 
module for the response, and it would be translated to a cleaner class in the core, but for this project that seems
excessive.) It also uses Mockito and JUnit for unit testing.

The user-client-adapter module is the http client for the API provided. It depends on SLF4J for logging, Jackson for
JSON deserialisation, and Wiremock and JUnit for unit testing.

Finally, the application-adapter module ties up the user-client-adapter and core, and sets up the dependency graph for
constructor injection. It also provides the web server. It depends on Spring Boot for the web server functionality.

Overall the code took around 4 or 5 hours (spread over the course of a day) to complete.

### Extensions

If I were to commit more time to this project, there are several things I'd like to address.

* Provide integration testing - including running the application, and using Wiremock to mock external API responses
* Better unit testing in the core module - there's potential for more testing there. Currently the code combining the
responses is not covered, but this could easily be done using Mockito.
* Provide application specific exceptions - as well as better error handling in general. More could be done in the 
user-client-adapter module to detect errors (in the city name provided, as well as in API responses), and provide
better error messages.

There are also multiple features that could be brought in, like support for the `/user/{id}` endpoint, and query
parameters for distance from London, e.t.c.
