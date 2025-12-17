package com.users.application.controllers;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class UsersControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private HttpEntity sendRequestANdGetResponse(String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + jwt);
        return new HttpEntity<>(null, headers);
    }

    @Nested
    class TestGetAllUsersMethod {
        @Test
        void testGetAllUsersMethod_getAllUsers() {
//when
            var jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbTJhaWwyQGVtYWlsLmNvbSIsImlhdCI6MTc2NTk2Njk1OCwiZXhwIjoxNzY1OTcwNTU4fQ.MmwsCL_2lZl_Tp6SYfyuzKS2lGDnJbDL_jeRerfUoQm5t8A1KEe_ay4qk2Z9lx0sdoLEWOC9K_aZ48f0_D3i5w";

            //Given
            var response =
                    restTemplate.exchange("/dev/api/users/getAllUsers", HttpMethod.GET, sendRequestANdGetResponse(jwt), List.class);

            System.out.println(response.getBody());
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        }


    }

    @Nested
    class TestGetUserByIdMethod {

        @Test
    void getUserById() {
    }
}

    @Test
    void getUserByFullName() {

    }

    /*
    v1 out of scope
     */
    @Test
    void getUserByIdNo() {
    }
}