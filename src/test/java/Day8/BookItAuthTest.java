package Day8;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.BeforeClass;
import utilities.ConfigurationReader;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;
import static org.testng.Assert.*;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

public class BookItAuthTest {

    @BeforeClass
    public void before(){
        RestAssured.baseURI = "https://cybertek-reservation-api-qa.herokuapp.com";
    }
    String accessToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxOTkxIiwiYXVkIjoic3R1ZGVudC10ZWFtLWxlYWRlciJ9.PejlP7F8p8bA9BGqol3jfWESaPRP5lBkIy7q2huN-4s";

    @Test
    public void getAllCampuses(){
        Response response = given().header("Authorization", accessToken)
                .when().get("/api/campuses");

        //response.prettyPrint();

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json");


    }
}
