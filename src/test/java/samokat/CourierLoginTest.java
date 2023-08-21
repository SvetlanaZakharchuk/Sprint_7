package samokat;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import samokat.Courier.CourierClient;
import samokat.models.Courier;

import static junit.framework.TestCase.assertEquals;
import static org.apache.http.HttpStatus.*;
import static samokat.Courier.CourierGenerator.randomCourier;
import static samokat.models.CourierCreds.credsFrom;
import static samokat.models.CourierCreds.credsFromPassword;

public class CourierLoginTest {

    private int id;
    private String expectedBodyAnswer;
    private CourierClient courierClient;

    @Before
    public void setUp() {

        courierClient = new CourierClient();
    }

    //курьер может авторизоваться
    @Test
    @DisplayName("courierLoginTest")
    public void courierLoginTest() {
        Courier courier = randomCourier();
        Response response = courierClient.create(courier);

        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());

        Response loginResponse = courierClient.login(credsFrom(courier));
        //получаем id курьера
        id = loginResponse.path("id");

        assertEquals("Курьер не залогинен", SC_OK, loginResponse.statusCode());
    }

    //авториация с неверным логином или паролем
    @Test
    @DisplayName("wrongLoginCourier")
    public void wrongLoginCourier() {
        Response loginResponse = courierClient.login("login", "password");
       //получаем тело ответа
        expectedBodyAnswer = loginResponse.path("message");

        assertEquals("Залогинился курьер с неверным логином", SC_NOT_FOUND, loginResponse.statusCode());
        assertEquals("Учетная запись не найдена", expectedBodyAnswer);
    }

    //авторизация без логина
    @Test
    @DisplayName("loginWithoutLogin")
    public void loginWithoutLogin() {
        Courier courier = randomCourier();
        Response response = courierClient.create(courier);

        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());

        Response passwordResponse = courierClient.login(credsFromPassword(courier));
        //получаем id курьера, чтобы удалить запись после теста
        id = courierClient.getCourierId(credsFrom(courier));
        //получаем тело ответа
        expectedBodyAnswer = passwordResponse.path("message");

        assertEquals("Курьер не залогинен", SC_BAD_REQUEST, passwordResponse.statusCode());
        assertEquals("Недостаточно данных для входа", expectedBodyAnswer);
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(id);
    }
}
