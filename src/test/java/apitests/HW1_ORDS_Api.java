package apitests;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;

public class HW1_ORDS_Api {

    /***
     * Q1:
     * - Given accept type is Json
     * - Path param value- US
     * - When users sends request to /countries
     * - Then status code is 200
     * - And Content - Type is Json
     * - And country_id is US
     * - And Country_name is United States of America
     * - And Region_id is 2
     */
    @Test
    public void q1Test(){

        given().accept(ContentType.JSON)
                .queryParam("q","{\"country_id\":\"US\"}")
                .when().get("http://54.210.58.84:1000/ords/hr/countries")
                .then().statusCode(200)
                .contentType("application/json")
                .body("items.country_id[0]",equalTo("US"),
                        "items.country_name[0]",equalTo("United States of America"),
                        "items.region_id[0]",equalTo(2));


    }

    /***
     * Q2:
     * - Given accept type is Json
     * - Query param value - q={"department_id":80}
     * - When users sends request to /employees
     * - Then status code is 200
     * - And Content - Type is Json
     * - And all job_ids start with 'SA'
     * - And all department_ids are 80
     * - Count is 25
     */

    @Test
    public void q2Test(){
        given().accept(ContentType.JSON)
                .queryParam("q","{\"department_id\":80}")
                .when().get("http://54.210.58.84:1000/ords/hr/employees")
                .then().statusCode(200)
                .contentType("application/json")
                .body("items.job_id",hasItems(startsWith("SA_")),
                        "items.department_id",hasItems(equalTo(80)),
                        "count",equalTo(25));


    }


    /***
     * Q3:
     * - Given accept type is Json
     * -Query param value q= region_id 3
     * - When users sends request to /countries
     * - Then status code is 200
     * - And all regions_id is 3
     * - And count is 6
     * - And hasMore is false
     * - And Country_name are;
     * Australia,China,India,Japan,Malaysia,Singapore
     */

    @Test
    public void RegionID3() {
        given().accept(ContentType.JSON)
                .queryParam("q", "{\"region_id\":3}")
                .when().get("http://3.81.99.109:1000/ords/hr/countries")
                .then()
                .statusCode(200)
                .body("items.region_id",hasItems(equalTo(3)),
                        "count", equalTo(6),
                        "hasMore", equalTo(false),
                        "items.country_name", hasItems("Australia","China","India","Japan","Malaysia","Singapore"));
    }
}
