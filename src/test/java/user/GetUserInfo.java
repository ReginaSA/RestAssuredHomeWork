package user;

import dto.User;
import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.data.JsonReader;
import io.restassured.response.Response;
import one.util.streamex.StreamEx;
import org.testng.annotations.Test;
import services.UserApi;

import static io.github.sskorol.data.TestDataReader.use;

public class GetUserInfo {


    private Object userId;

    @DataSupplier(name = "user")
    public StreamEx<User> getUsers() {
        return use(JsonReader.class).withTarget(User.class).withSource("datajson/user.json").read();
    }

    @Test(dataProvider = "user")
    public void getUserById(User user) {
        User newUser = User.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phone(user.getPhone())
                .build();

        UserApi userApi = new UserApi();
        Response response = userApi.createUser(newUser);
        userId = response.jsonPath().get("message");
        System.out.println(userId);
    }

    /**
     * Тест получения опльзователя по имени firstName
     */
    @Test(dataProvider = "user")
    public void getUserByNme(User user) {
        User newUser = User.builder()
                .username(user.getUsername())
                .build();

        UserApi userApi = new UserApi();
        userApi.getUser(newUser)
                .then()
                .log().all()
                .statusCode(200);
    }
}
