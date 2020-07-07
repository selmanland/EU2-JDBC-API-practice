package apitests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SpartanGetRequest {

    String spartanurl = "http://54.210.58.84:8000";

    @Test
    public void test1(){

        Response response = given().accept(ContentType.JSON).and()
                .auth().basic("admin","admin")
                .when().get(spartanurl + "/api/spartans");

        System.out.println(response.statusCode());

        response.prettyPrint();

    }

       /* TASK
        When users sends a get request to /api/spartans/3
        Then status code should be 200
        And content type should be application/json;charset=UFT-8
        and json body should contain Fidole
     */

       @Test
    public void test2(){

           Response response = given().auth().basic("admin", "admin")
                   .when().get(spartanurl + "/api/spartans/3");

           //verify status code
           assertEquals(response.statusCode(),200);

           //verify content type
           assertEquals(response.contentType(),"application/json;charset=UTF-8");

           //verify body contains Fidole
           assertTrue(response.body().asString().contains("Fidole"));

       }

       /*
        Given no headers provided
        When Users sends GET request to /api/hello
        Then response status code should be 200
        And Content type header should be “text/plain;charset=UTF-8”
        And header should contain date
        And Content-Length should be 17
        And body should be “Hello from Sparta"
        */

       @Test
        public void helloTest(){
           //request
           Response response = when().get(spartanurl + "/api/hello");

           //verify status code
           assertEquals(response.statusCode(),200);
           //verify content-type
           assertEquals(response.contentType(),"text/plain;charset=UTF-8");
           //verify we have headers named Date
           assertTrue(response.headers().hasHeaderWithName("Date"));

           //------get any header with following methods
           System.out.println(response.header("Date"));
           System.out.println(response.getHeader("Content-Length"));

           //verify content length is 17
            assertEquals(response.getHeader("Content-Length"),"17");

            //verify body contains "Hello from Sparta"
            assertTrue(response.body().asString().contains("Hello from Sparta"));

       }


}
