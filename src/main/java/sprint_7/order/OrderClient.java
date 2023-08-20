package sprint_7.order;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.File;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_URI = "http://qa-scooter.praktikum-services.ru/";
    private static final String ORDER_URI = "/api/v1/orders";


    public OrderClient() {
        RestAssured.baseURI = BASE_URI;
    }

    //создаем заказ
    public Response create(File json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post(ORDER_URI);
    }

    //получаем список заказов
    public Response getOrdersList() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .get(ORDER_URI);
    }


}
