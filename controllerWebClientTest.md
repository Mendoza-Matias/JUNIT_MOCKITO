# Test de controllerWebClient

Ejemplo de codigo utilizando Junit5 y Mockito 

```java
package com.mmendoza.employee_system.presentation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) /*define random port*/
class EmployeesControllerWebClientTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getAllEmployees() {
        webTestClient.get().uri("http://localhost:8081/api/employees") /*performs the query to the endpoint*/
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].dni").isEqualTo(123)
        ;
    }
}
```