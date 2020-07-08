package apitests;

import org.testng.annotations.Test;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;

public class HW2_Spartan_Api {

    /***
     * Q1:
     * Given accept type is json
     * And path param id is 20
     * When user sends a get request to "/spartans/{id}"
     * Then status code is 200
     * And content-type is "application/json;char"
     * And response header contains Date
     * And Transfer-Encoding is chunked
     * And response payload values match the following:
     * id is 20,
     * name is "Lothario",
     * gender is "Male",
     * phone is 7551551687
     */

    @Test
    public void q1Test(){
        given().accept(ContentType.JSON)
                .pathParam("id",20)
                .auth().basic("admin","admin")
                .when().get("http://54.210.58.84:8000/spartans/{id}")
                .then().statusCode(200)
                .header("Content-Type","application/json;charset=UTF-8")
                .header("Date",notNullValue())
                .header("Transfer-Encoding","chunked")
                .body("id",is(20),
                        "name",is("Lothario"),
                "gender",is("Male"),
                        "phone",is(7551551687L));

    }

    /***
     * Q2:
     * Given accept type is json
     * And query param gender = Female
     * And queary param nameContains = r
     * When user sends a get request to "/spartans/search"
     * Then status code is 200
     * And content-type is "application/json;char"
     * And all genders are Female
     * And all names contains r
     * And size is 20
     * And totalPages is 1
     * And sorted is false
     */
}
