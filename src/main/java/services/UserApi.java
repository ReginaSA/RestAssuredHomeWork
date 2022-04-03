package services;

import dto.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserApi {
    private static final String BASE_URI = "https://petstore.swagger.io/v2/";
    private static final String USER = "/user";
    private final RequestSpecification spec;

    public UserApi() {
        spec = given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON);
    }

    public Response createUser(User user) {
        return
                given(spec)
                        .with()
                        .body(user)
                        .log().all()
                        .when()
                        .post(USER);
    }

    public Response getUser(User user) {
        return
                given(spec)
                .log().all()
                .when()
                .get(USER + "/" + user.getUsername());
    }
}
