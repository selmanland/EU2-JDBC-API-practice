package Day8;

import io.restassured.RestAssured;
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
public class JsonSchemaValidation {

    @BeforeClass
    public void beforeClass(){
        RestAssured.baseURI = ConfigurationReader.getProperty("spartanapi_url");
    }

    @Test
    public void JsonSchemaValidationForSpartan(){
        given().accept(ContentType.JSON)
                .and().auth().basic("admin","admin")
                .pathParam("id",50)
                .when().get("/api/spartans/{id}").then()
                .assertThat().statusCode(200)
                .and().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("SingleSpartanSchema.json"));


    }
}