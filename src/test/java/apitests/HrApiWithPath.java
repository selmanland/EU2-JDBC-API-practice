package apitests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class HrApiWithPath {


    @BeforeClass
    public void beforeClass(){
        RestAssured.baseURI = ConfigurationReader.getProperty("hrapi_url");
    }

    @Test
    public void getCountriesWithPath(){
        Response response = given().queryParam("q", "{\"region_id\": 2}")
                .when().get("/countries");

        assertEquals(response.statusCode(),200);

        //print count value
        System.out.println("response.path(\"count\") = " + response.path("count"));
        //print hasMore
        System.out.println(response.path("hasMore").toString());
        /*items.country_id[0]   --> AR
          items.country_name[1] -->Brazil
          items.links[2].href[0] --> http://54.210.58.84:1000/ords/hr/countries/CA
          items.country_name -->get all countries name
         */

        String firstCountryId = response.path("items.country_id[0]");
        System.out.println("firstCountryId = " + firstCountryId);

        String secondCountryName = response.path("items.country_name[1]");
        System.out.println("secondCountryName = " + secondCountryName);

       String link2 = response.path("items.links[2].href[0]");
       System.out.println("link2 = " + link2);

       //get all countries
        List<String> allCountries = response.path("items.country_name");
        System.out.println("allCountries.size() = " + allCountries.size());

        for (String country : allCountries) {
            System.out.println(country);
        }

        //TASK
        //assert that all regions id's are equal to 2
        List<Integer> allRegionsIds = response.path("items.region_id");

        for (int regionsId : allRegionsIds) {
            System.out.println("regionsId = " + regionsId);
            assertEquals(regionsId,2);
        }

    }

    @Test
    public void test2(){
        Response response = given().accept(ContentType.JSON)
                .and().queryParam("q", "{\"job_id\": \"IT_PROG\"}")
                .when().get("/employees");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json");
        assertTrue(response.asString().contains("IT_PROG"));
        response.prettyPrint();

        //get all JOB IDs
        List<String> allJobIDs = response.path("items.job_id");
        //verify each of your job ids is equal to IT_PROG
        for (String jobID : allJobIDs) {
            assertEquals(jobID,"IT_PROG");
        }

    }



}
