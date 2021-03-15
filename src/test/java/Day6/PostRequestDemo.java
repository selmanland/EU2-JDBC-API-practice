package Day6;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import utilities.ConfigurationReader;
import java.util.HashMap;
import java.util.Map;

public class PostRequestDemo {
    @BeforeClass
    public void beforeClass(){
        RestAssured.baseURI = ConfigurationReader.getProperty("spartanapi_url");
    }

 /*
    Given accept type and Content type is JSON
    And request json body is:
    {
      "gender":"Male",
      "name":"MikeEU",
      "phone":8877445596
   }
    When user sends POST request to '/api/spartans'
    Then status code 201
    And content type should be application/json
    And json payload/response should contain:
    "A Spartan is Born!" message
    and same data what is posted
 */

    @Test
    public void PostNewSpartan(){
        String jsonBody ="{\n" +
                "      \"gender\":\"Male\",\n" +
                "      \"name\":\"MikeEU\",\n" +
                "      \"phone\":8877445596\n" +
                "   }";

        Response response = given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .and().auth().basic("admin", "admin")
                .and().body(jsonBody)
                .when().post("/api/spartans");

        //validations...
        //verify status code
        assertEquals(response.statusCode(),201);
        //verify content type
        assertEquals(response.contentType(),"application/json");

        //verify message
        String message = response.path("success");
        assertEquals(message,"A Spartan is Born!");

        String name = response.path("data.name");
        String gender = response.path("data.gender");
        long phone =response.path("data.phone");

        //assertion
        assertEquals(name,"MikeEU");
        assertEquals(gender,"Male");
        assertEquals(phone,8877445596l);

    }

    @Test
    public void PostNewSpartan2(){
        //create map to keep request json body info
        Map<String,Object> requestMap = new HashMap<>();
        //adding values that we want to post
        requestMap.put("name","MikeEU");
        requestMap.put("gender","Male");
        requestMap.put("phone",8877445596l);

        Response response = given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .and().auth().basic("admin", "admin")
                .and().body(requestMap)
                .when().post("/api/spartans");

        //verify status code
        assertEquals(response.statusCode(),201);
        //verify content type
        assertEquals(response.contentType(),"application/json");

        //verify message
        String message = response.path("success");
        assertEquals(message,"A Spartan is Born!");

        String name = response.path("data.name");
        String gender = response.path("data.gender");
        long phone =response.path("data.phone");

        //assertion
        assertEquals(name,"MikeEU");
        assertEquals(gender,"Male");
        assertEquals(phone,8877445596l);

        int idFromPOST = response.path("data.id");
        //===========END OF POST REQUEST
        //after post the new spartan, we want to send get request
        given().pathParam("id",idFromPOST)
                .and().auth().basic("admin","admin")
                .and().accept(ContentType.JSON)
                .when().get("/api/spartans/{id}")
                .then().assertThat().statusCode(200).log().all();
    }

    @Test
    public void PostNewSpartan3(){
        //get spartan info from mackaroo and send post requests(optional homework)
        //-Create one mackaroo api for name,gender,phone
        //send get request to retrieve random info from api
        //use those info to send post request to spartan


        //create spartan object to send information
        Spartan spartanEU = new Spartan();
        spartanEU.setName("MikeEU");
        spartanEU.setGender("Male");
        spartanEU.setPhone(8877445596l);


        Response response = given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .and().auth().basic("admin", "admin")
                .and().body(spartanEU)
                .when().post("/api/spartans");

        //verify status code
        assertEquals(response.statusCode(),201);
        //verify content type
        assertEquals(response.contentType(),"application/json");

        //verify message
        String message = response.path("success");
        assertEquals(message,"A Spartan is Born!");

        String name = response.path("data.name");
        String gender = response.path("data.gender");
        long phone =response.path("data.phone");

        //assertion
        assertEquals(name,spartanEU.getName());
        assertEquals(gender,spartanEU.getGender());
        assertEquals(phone,spartanEU.getPhone());

        int idFromPOST = response.path("data.id");
        //===========END OF POST REQUEST
        //after post the new spartan, we want to send get request
        given().pathParam("id",idFromPOST)
                .and().auth().basic("admin","admin")
                .and().accept(ContentType.JSON)
                .when().get("/api/spartans/{id}")
                .then().assertThat().statusCode(200).log().all();
    }




}