package user;

import dto.User;
import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.data.JsonReader;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import one.util.streamex.StreamEx;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.UserApi;

import static io.github.sskorol.data.TestDataReader.use;

public class CreateUserTest {

    private String userId;

    @DataSupplier(name = "user")
    public StreamEx<User> getUsers() {
        return use(JsonReader.class).withTarget(User.class).withSource("datajson/user.json").read();
    }

    @DataSupplier(name = "emptyUser")
    public StreamEx<User> getIncorrectUsers() {
        return use(JsonReader.class).withTarget(User.class).withSource("datajson/emptyUser.json").read();
    }

    /**
     * Добавляем пользователя по API с корректными данными
     * Проверяет код ответа сервиса 200
     * Проверяем соответствие ответа сервиса json schema
     * Проверяем, что пользователь создался и присвоен ID
     */
    @Test(dataProvider = "user")
    public void addNewUser(User user) {
        User newUser = User.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phone(user.getPhone())
                .build();

        UserApi userApi = new UserApi();
        userApi.createUser(newUser)
                .then()
                .log().all()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/CreateUser.json"))
                .statusCode(200);

        Response response = userApi.createUser(newUser);
        userId = response.jsonPath().get("message");
        Assert.assertTrue(userId != "0", "Был создан пользователь c id:" + userId);
    }

    /**
     * Добавляем нового пользователя по API с пустыми данными
     * Проверяем ответ сервиса 200
     * Проверяем соответствие ответа сервиса json schema
     * Проверяем, что пользователь не создался и пришел ID = 0
     */
    @Test(dataProvider = "emptyUser")
    public void addEmptyUser(User user) {
        User newUser = User.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phone(user.getPhone())
                .build();

        UserApi userApi = new UserApi();
        userApi.createUser(newUser)
                .then()
                .log().all()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/CreateUser.json"))
                .statusCode(200);
    }

}
