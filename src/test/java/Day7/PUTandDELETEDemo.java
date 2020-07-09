package Day7;

import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;

import io.restassured.http.ContentType;
import utilities.ConfigurationReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PUTandDELETEDemo {

    @BeforeClass
    public void beforeClass(){
        RestAssured.baseURI = ConfigurationReader.getProperty("spartanapi_url");
    }


    @Test
    public void PUTTest(){
        //create one map for the put request json body
        Map<String,Object> putMap = new HashMap<>();
        putMap.put("name","PutName");
        putMap.put("gender","Male");
        putMap.put("phone",12312312312l);

        ValidatableResponse response = given().contentType(ContentType.JSON)
                .and().auth().basic("admin", "admin")
                .and().pathParam("id", 340)
                .and().body(putMap)
                .when().put("/api/spartans/{id}").then()
                .assertThat().statusCode(204);


    }

    @Test
    public void DeleteSpartan(){
        //delete 1- 300 spartans randomly
        Random rn = new Random();

        int idToDelete = rn.nextInt(300)+1;
        System.out.println("Spartan id will be deleted: "+idToDelete);
        given().pathParam("id",idToDelete)
                .and().auth().basic("admin","admin")
                .when().delete("/api/spartans/{id}")
                .then().statusCode(204);
    }


    //test Patch method with only changing phone number of Meta
    @Test
    public void PATCHTest(){
        //create one map for the put request json body
        Map<String,Object> putMap = new HashMap<>();

        putMap.put("phone",12312312312l);

        ValidatableResponse response = given().contentType(ContentType.JSON)
                .and().auth().basic("admin", "admin")
                .and().pathParam("id", 340)
                .and().body(putMap)
                .when().patch("/api/spartans/{id}").then()
                .assertThat().statusCode(204);


    }





}
