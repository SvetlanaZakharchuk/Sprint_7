package samokat.Courier;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import samokat.models.Courier;
import samokat.models.CourierCreds;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final String BASE_URI = "http://qa-scooter.praktikum-services.ru/";
    private static final String CREATE_URI = "api/v1/courier";
    private static final String LOGIN_URI = "api/v1/courier/login";
    private static final String DELETE_URI = "/api/v1/courier/{id}";

    public CourierClient() {
        RestAssured.baseURI = BASE_URI;
    }

    //создаем курьера

    @Step("Создание курьера")
    public Response create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(CREATE_URI);
    }
    //логинися
    @Step("Авторизация курьера")
    public Response login(CourierCreds creds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(creds)
                .when()
                .post(LOGIN_URI);
    }
    //логинимся с другими логином и паролем
    @Step("Авторизация с несуществующими логином и паролем")
    public Response login(String login, String password) {
        CourierCreds creds = new CourierCreds(login, password);
        return login(creds);
    }
    //получаем id курьера, чтобы удалить запись после теста
    @Step("Получение id курьера, чтобы удалить запись после выполения теста")
    public int getCourierId(CourierCreds creds) {

        return login(creds).path("id");
    }

    //удаляем курьера
    @Step("Удаление курьера")
    public Response deleteCourier(int id) {
        return given()
                .delete(DELETE_URI, id);
    }
}
