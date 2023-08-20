package sprint_7;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sprint_7.Courier.CourierClient;
import sprint_7.models.Courier;

import static junit.framework.TestCase.assertEquals;
import static org.apache.http.HttpStatus.*;
import static sprint_7.Courier.CourierGenerator.randomCourier;
import static sprint_7.models.CourierCreds.credsFrom;
import static sprint_7.models.CourierCreds.credsFromPassword;


public class CourierLoginTest {

    private int id;
    private CourierClient courierClient;

    @Before
    public void SetUp() {

        courierClient = new CourierClient();
    }

    //курьер может авторизоваться
    @Test
    public void CourierLoginTest() {
        Courier courier = randomCourier();
        Response response = courierClient.create(courier);
        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());
        Response loginResponse = courierClient.login(credsFrom(courier));

        id = loginResponse.path("id");
        assertEquals("Курьер не залогинен", SC_OK, loginResponse.statusCode());

    }

    //авториация с неверным логином или паролем
    @Test
    public void WrongLoginCourier() {
        Response loginResponse = courierClient.login("login", "password");
        assertEquals("Залогинился курьер с неверным логином", SC_NOT_FOUND, loginResponse.statusCode());

    }

    //авторизация без логина
    @Test
    public void loginWithoutLogin() {
        Courier courier = randomCourier();
        Response response = courierClient.create(courier);
        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());
        Response passwordResponse = courierClient.login(credsFromPassword(courier));
        assertEquals("Курьер не залогинен", SC_BAD_REQUEST, passwordResponse.statusCode());

        //получаем id курьера, чтобы удалить запись после теста
        id = courierClient.getCourierId(credsFrom(courier));
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(id);
    }

}
