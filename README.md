# Memsource project loader


## Description

### Setup Page

Memsource account can be configured here.
* The configuration should be represented as a domain class.
* Two text fields for username and password.
* Configuration can be edited and must be saved on a persistent storage (H2 database, for example).
* No need to care about the security of a password.

### Projects Page
* List projects retrieved from https://cloud.memsource.com/web/api/v3/project/list?token=TOKEN
* A token is needed from https://cloud.memsource.com/web/api/v3/auth/login?userName=USERNAME&password=PASSWORD
* Name, status, source language and target languages should be displayed.
* The projects are loaded and rendered in JavaScript.
* An endpoint is implemented that provides the data for an AJAX call.

## Running instructions

* Maven is needed to run the application.
* Start up the app using (`mvn spring-boot:run`).
* Open up a tab and navigate to http://localhost:8080.

## Used technologies

* Spring Boot https://projects.spring.io/spring-boot/
* Spring WebFlux, reactor.io https://docs.spring.io/spring/docs/5.0.0.BUILD.../spring.../html/web-reactive.html
* Lombok https://projectlombok.org
* ReactJS https://reactjs.org/
