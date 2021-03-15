package apitests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SpartanTestWithParameters {

    @BeforeClass
    public void beforeClass(){
        RestAssured.baseURI = "http://3.84.148.128:8000";
    }

    /*
      Given accept type is Json
      And Id parameter value is 5
      When user sends GET request to /api/spartans/{id}
      Then response status code should be 200
      And response content-type: application/json;charset=UTF-8
      And "Blythe" should be in response payload
   */

    @Test
    public void getSpartanId_Positive_PathParam(){
        Response response = given().accept(ContentType.JSON)
                .and().auth().basic("admin", "admin")
                .and().pathParam("id",5)
                .when().get("/api/spartans/{id}");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json");
        assertTrue(response.body().asString().contains("Blythe"));

    }

      /*
        TASK
        Given accept type is Json
        And Id parameter value is 500
        When user sends GET request to /api/spartans/{id}
        Then response status code should be 404
        And response content-type: application/json;charset=UTF-8
        And Spartan Not Found" message should be in response payload
     */

    @Test
    public void getSpartanId_Negative_PathParam(){
        Response response = given().accept(ContentType.JSON)
                .and().auth().basic("admin", "admin")
                .and().pathParam("id",500)
                .when().get("/api/spartans/{id}");

        assertEquals(response.statusCode(),404);
        assertEquals(response.contentType(),"application/json");
        assertTrue(response.body().asString().contains("Not Found"));

    }


    /*
        Given accept type is Json
        And query parameter values are :
        gender|Female
        nameContains|e
        When user sends GET request to /api/spartans/search
        Then response status code should be 200
        And response content-type: application/json;charset=UTF-8
        And "Female" should be in response payload
        And "Janette" should be in response payload
     */

    @Test
    public void positiveTestWithQueryParam(){
        Response response = given().accept(ContentType.JSON)
                .and().auth().basic("admin", "admin")
                .and().queryParam("gender", "Female")
                .queryParam("nameContains", "e")
                .when().get("/api/spartans/search");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json;charset=UTF-8");
        //verify body contains
        assertTrue(response.body().asString().contains("Female"));
        assertTrue(response.body().asString().contains("Janette"));
    }

    @Test
    public void positiveTestWithQueryParamWithMaps(){
        //create a map and add parameters
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("gender","Female");
        queryMap.put("nameContains","e");

        Response response = given().accept(ContentType.JSON).and()
                .auth().basic("admin", "admin")
                .queryParams(queryMap)
                .when().get("/api/spartans/search");

        //response verification
        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json;charset=UTF-8");
        //verify body contains
        assertTrue(response.body().asString().contains("Female"));
        assertTrue(response.body().asString().contains("Janette"));
    }



}
