package ca.gbc.productservice;

import io.restassured.RestAssured;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;


//Tells Spring Boot to look for a main configuration class(@SpringBootApplication)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {


    //This annotation is used in combination with testContainer to automatically configure the connection to
    //test mongodbContainer.
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.0.10");


    //variable storing Local server port, so we can use in this class.
    @LocalServerPort
    private Integer port;

    //https:localhost:port/api/product


    @BeforeEach // run this setup before each test.
    void setup(){
        RestAssured.baseURI = "http:localhost";
        RestAssured.port = port;
    }

    static{
        mongoDBContainer.start();
    }

    @Test
    void createProductTest(){

        String requestBody = """
                {
                
                "name" : "Samsung Tv",
                "description" : "Samsung Tv - Model 2024",
                "price" : "2000";
       
                }
                """;

        //BDD - 0 behavioural Driven Development(Given, When, Then)
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id",  Matchers.notNullValue())
                .body("name", Matchers.equalTo("Samsung Tv"))
                .body("description",Matchers.equalTo("Samsung Tv - Model 2024"))
                .body("price",Matchers.equalTo(2000));

    }
    @Test
    void getAllProductTest(){

        String requestBody = """
                {
                
                "name" : "Samsung Tv",
                "description" : "Samsung Tv - Model 2024",
                "price" : "2000";
       
                }
                """;

        //BDD - 0 behavioural Driven Development(Given, When, Then)
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id",  Matchers.notNullValue())
                .body("name", Matchers.equalTo("Samsung Tv"))
                .body("description",Matchers.equalTo("Samsung Tv - Model 2024"))
                .body("price",Matchers.equalTo(2000));


        RestAssured.given().contentType("application/json")
                .when()
                .get("/api/product")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()",Matchers.greaterThan(0))
                .body("[0].name", Matchers.equalTo("Samsung Tv"))
                .body("[0].description",Matchers.equalTo("Samsung Tv - Model 2024"))
                .body("[0].price",Matchers.equalTo(2000));
    }

}
