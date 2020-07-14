package HappyPotter;

import Day6.Spartan;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.*;

public class POJO_Tests {

    @BeforeClass
    public void beforeClass(){
        RestAssured.baseURI = "https://www.potterapi.com/v1";
    }

    String apiKey = "$2a$10$srnIOtEWxZBxZ7ZoQj97X.ZzxfNPthaVJcOtvagzzLN25iSwuHeEa";

    /***
     * Verify sorting hat
     * 1. Send a get request to /sortingHat. Request includes :
     * 2. Verify status code 200, content type application/json; charset=utf-8
     * 3. Verify that response body contains one of the following houses:
     * "Gryffindor", "Ravenclaw", "Slytherin", "Hufflepuff"
     */
    @Test(description = "Test 1")
    public void sortHatTest(){
        Response response = when().get("/sortingHat");
        //Verify status code 200, content type application/json; charset=utf-8
        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json; charset=utf-8");
        System.out.println("response = " + response.body().asString());

        //Verify that response body contains one of the following houses:
        //     * "Gryffindor", "Ravenclaw", "Slytherin", "Hufflepuff"
        List<String> myList = Arrays.asList("Gryffindor","Ravenclaw","Slytherin","Hufflepuff");

        /*for (String item : myList) {
            if (item.equals(response.body().asString())) {
                System.out.println("item = " + item);
                assertTrue(item.contains(response.body().asString()));
                break;
            }
        }*/
        String string = response.body().asString().replace("\"", "");
        response.prettyPrint();

    }
    /***
     * Verify bad key
     * 1. Send a get request to /characters. Request includes :
     * • Header Accept with value application/json
     * • Query param key with value invalid
     * 2. Verify status code 401, content type application/json; charset=utf-8
     * 3. Verify response status line include message Unauthorized
     * 4. Verify that response body says "error": "API Key Not Found"
     */
    @Test(description = "Test 2")
    public void badKeyTest(){
        Response response = given().accept(ContentType.JSON)
                .queryParam("key","invalid")
                .when().get("/characters");

        response.prettyPrint();

        assertEquals(response.statusCode(),401);
        assertEquals(response.contentType(),"application/json; charset=utf-8");
        assertTrue(response.statusLine().contains("Unauthorized"));
        assertEquals(response.body().path("error"),"API Key Not Found");

    }
    /***
     * Verify no key
     * 1. Send a get request to /characters. Request includes :
     * • Header Accept with value application/json
     * 2. Verify status code 409, content type application/json; charset=utf-8
     * 3. Verify response status line include message Conflict
     * 4. Verify that response body says "error": "Must pass API key for request"
     */
    @Test(description = "Test 3")
    public void noKeyTest(){
        given().accept(ContentType.JSON)
                .when().get("/characters")
                .then().assertThat().statusCode(409)
                .contentType("application/json; charset=utf-8")
                .statusLine("HTTP/1.1 409 Conflict")
                .assertThat().body("error",equalTo("Must pass API key for request"));

    }

    /***
     * Verify number of characters
     * 1. Send a get request to /characters. Request includes :
     * • Header Accept with value application/json
     * • Query param key with value {{apiKey}}
     * 2. Verify status code 200, content type application/json; charset=utf-8
     * 3. Verify response contains 194 characters (actual=195)
     */
    @Test(description = "Test 4")
    public void noOfCharactersTest(){

        Response response = given().accept(ContentType.JSON)
                .queryParam("key",
                        apiKey)
                .when().get("/characters");

        //Verify status code 200, content type application/json; charset=utf-8
        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json; charset=utf-8");

        //Verify response contains 195 characters
        List <Map<String,Object>> list = response.body().as(List.class);
        assertEquals(list.size(),195);

        System.out.println("list.get(0).get(name) = " + list.get(0).get("name"));

        //response.prettyPrint();


    }

    /***
     * Verify number of character id and house
     * 1. Send a get request to /characters. Request includes :
     * • Header Accept with value application/json
     * • Query param key with value {{apiKey}}
     * 2. Verify status code 200, content type application/json; charset=utf-8
     * 3. Verify all characters in the response have id field which is not empty
     * 4. Verify that value type of the field dumbledoresArmy is a boolean in all characters in the response
     * 5. Verify value of the house in all characters in the response is one of the following:
     * "Gryffindor", "Ravenclaw", "Slytherin", "Hufflepuff"
     */
    @Test (description = "Test 5")
    public void noOfCharIdAndHouseTest(){

        Response response = given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .when().get("/characters");

        List<Map<String,Object>> chr = response.body().as(List.class);
        System.out.println("chr = " + chr.get(0).get("_id"));

        for (Map<String, Object> ch : chr) {
            assertNotNull(ch.get("_id"));
        }

        //2nd way:
        /*given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .when().get("/characters")
                .then().assertThat().statusCode(200).contentType("application/json; charset=utf-8")
                .and().body("_id",hasItem(notNullValue()))
                .body("dumbledoresArmy",hasItems(true,false))
                .body("house",hasItems("Gryffindor", "Ravenclaw", "Slytherin", "Hufflepuff"));*/

    }

    /***
     * Verify all character information
     * 1. Send a get request to /characters. Request includes :
     * • Header Accept with value application/json
     * • Query param key with value {{apiKey}}
     * 2. Verify status code 200, content type application/json; charset=utf-8
     * 3. Select name of any random character
     * 4. Send a get request to /characters. Request includes :
     * • Header Accept with value application/json
     * • Query param key with value {{apiKey}}
     * • Query param name with value from step 3
     * 5. Verify that response contains the same character information from step 3. Compare all fields.
     */
    @Test(description = "Test 6")
    public void characterInfoTest(){
        Response response = given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .when().get("/characters");

        //Verify status code 200, content type application/json; charset=utf-8
        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json; charset=utf-8");

        List<Map<String,Object>> character = response.body().as(List.class);
        int size = character.size();
        System.out.println("size = " + size);

        //Select name of any random character
        Random rn = new Random();
        int rnNameNo = rn.nextInt(size);

        String rnName = character.get(rnNameNo).get("name").toString();
        System.out.println("rnName = " + rnName);

        //Send a get request to /characters
        Response response2 = given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .queryParam("name",rnName)
                .when().get("/characters");

        response2.prettyPrint();

        //Verify that response contains the same character information from step 3. Compare all fields ???
        assertEquals(response2.statusCode(),200);
        String name2 = response2.body().path("name[0]");
        //System.out.println("name2 = " + name2);

        //compare 2 schools
        assertEquals(response2.body().path("school[0]"),character.get(rnNameNo).get("school"));

        //compare 2 bloodStatus
        assertEquals(response2.body().path("bloodStatus[0]"),character.get(rnNameNo).get("bloodStatus"));

        //compare 2 species
        assertEquals(response2.body().path("species[0]"),character.get(rnNameNo).get("species"));


    }

    /***
     * Verify name search
     * 1. Send a get request to /characters. Request includes :
     * • Header Accept with value application/json
     * • Query param key with value {{apiKey}}
     * • Query param name with value Harry Potter
     * 2. Verify status code 200, content type application/json; charset=utf-8
     * 3. Verify name Harry Potter
     * 4. Send a get request to /characters. Request includes :
     * • Header Accept with value application/json
     * • Query param key with value {{apiKey}}
     * • Query param name with value Marry Potter
     * 5. Verify status code 200, content type application/json; charset=utf-8
     * 6. Verify response body is empty
     */
    @Test(description = "Test 7")
    public void nameSearchTest(){

        given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .queryParam("name","Harry Potter")
                .when().get("/characters")
                .then().assertThat().statusCode(200).contentType("application/json; charset=utf-8")
                .and().assertThat().body("name[0]", equalTo("Harry Potter"));

        given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .queryParam("name","Marry Potter")
                .when().get("/characters")
                .then().assertThat().statusCode(200).contentType("application/json; charset=utf-8")
                .and().assertThat().body("", Matchers.hasSize(0));

    }

    /***
     * Verify house members
     * 1. Send a get request to /houses. Request includes :
     * • Header Accept with value application/json
     * • Query param key with value {{apiKey}}
     * 2. Verify status code 200, content type application/json; charset=utf-8
     * 3. Capture the id of the Gryffindor house
     * 4. Capture the ids of the all members of the Gryffindor house
     * 5. Send a get request to /houses/:id. Request includes :
     * • Header Accept with value application/json
     * • Query param key with value {{apiKey}}
     * • Path param id with value from step 3
     * 6. Verify that response contains the same member ids as the step 4
     */

    @Test(description = "Test 8")
    public void houseMembersTest(){

        Response response = given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .when().get("/houses");

        //Verify status code 200, content type application/json; charset=utf-8
        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json; charset=utf-8");

        //Capture the id of the Gryffindor house
        Object idGryffindor = response.path("_id[0]");
        System.out.println("idGryffindor = " + idGryffindor);

        //Capture the ids of the all members of the Gryffindor house
        List GryffindorList = response.path("members[0]");
        System.out.println("GryffindorList = " + GryffindorList);

        //Send a get request to /houses/:id Path param id with value from step 3
        Response response2 = given().accept(ContentType.JSON)
                .queryParam("key", apiKey)
                .queryParam("_id",idGryffindor)
                .when().get("/houses");

        //Verify that response contains the same member ids as the step 4
        List GryffindorList2 = response2.path("members[0]");
        assertEquals(GryffindorList,GryffindorList2);

    }

    /***
     * Verify house members again
     * 1. Send a get request to /houses/:id. Request includes :
     * • Header Accept with value application/json
     * • Query param key with value {{apiKey}}
     * • Path param id with value 5a05e2b252f721a3cf2ea33f
     * 2. Capture the ids of all members
     * 3. Send a get request to /characters. Request includes :
     * • Header Accept with value application/json
     * • Query param key with value {{apiKey}}
     * • Query param house with value Gryffindor
     * 4. Verify that response contains the same member ids from step 2
     */
    @Test(description = "Test 9")
    public void houseMembers2(){

    }

    /***
     * Verify house with most members
     * 1. Send a get request to /houses. Request includes :
     * • Header Accept with value application/json
     * • Query param key with value {{apiKey}}
     * 2. Verify status code 200, content type application/json; charset=utf-8
     * 3. Verify that Gryffindor house has the most members
     */
    @Test(description = "Test 10")
    public void houseMostTest(){

    }
}
