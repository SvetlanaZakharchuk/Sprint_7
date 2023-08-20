package sprint_7;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sprint_7.Courier.CourierClient;
import sprint_7.models.Courier;

import static org.junit.Assert.assertEquals;
import static sprint_7.Courier.CourierGenerator.courierWithoutPassword;
import static sprint_7.Courier.CourierGenerator.randomCourier;
import static sprint_7.models.CourierCreds.credsFrom;

public class CourierCreateTest {

    private int id;
    private CourierClient courierClient;

    @Before
    public void SetUp() {
        courierClient = new CourierClient();
    }

    //тестируем создание курьера
    @Test
    public void CreateCourierTest() {

        Courier courier = randomCourier();
        Response response = courierClient.create(courier);


        assertEquals("Неверный статус код", HttpStatus.SC_CREATED, response.statusCode());

        //получаем id курьера, чтобы удалить запись после теста
        id = courierClient.getCourierId(credsFrom(courier));
    }

    //проверка, что нельзя создать двух одинаковых курьеров
    @Test
    public void CreateTwoCouriersTest() {
        Courier courier = randomCourier();
        Response response = courierClient.create(courier);//создали одного курьера
        assertEquals(HttpStatus.SC_CREATED, response.statusCode());//проверили, что курьер создан
        Response response_other = courierClient.create(courier);//создаем еще одного курьера с теми же логином и паролем
        assertEquals("Создались две одинаковые записи", HttpStatus.SC_CONFLICT, response_other.statusCode());//проверяем, что произошел конфликт

        //получаем id курьера, чтобы удалить запись после теста
        id = courierClient.getCourierId(credsFrom(courier));
    }

    //проверка, что нельзя создать курьера с одним и тем же логином
    @Test
    public void CreateCourierWithTheSameLogin() {
        Courier courier = randomCourier();
        Response response = courierClient.create(courier);
        //создаем второго курьера с тем же логином и другими паролем и именем
        Courier courierSameLogin = new Courier().withLogin(courier.getLogin()).withPassword("32323").withFirstName("ehehe");
        Response responseSameLogin = courierClient.create(courierSameLogin);
        assertEquals("Создались два курьера с одним и тем же логином", HttpStatus.SC_CONFLICT, responseSameLogin.statusCode());

        //получаем id курьера, чтобы удалить запись после теста
        id = courierClient.getCourierId(credsFrom(courier));
    }

    //проверка, что нельзя создать курьера, если не все поля переданы в запрос
    @Test
    public void CreateCourierNOFirstNameTest() {
        Courier courier = courierWithoutPassword();
        Response response = courierClient.create(courier);
        assertEquals("Содался курьер, без пароля", HttpStatus.SC_BAD_REQUEST, response.statusCode());

    }

    //проверка, что код ответа при создании курьера ok:true
    @Test
    public void CreateCourierAndCheckAnswerTrueTest() {
        Courier courier = randomCourier();
        Response response = courierClient.create(courier);
        assertEquals("Получили неожидаемое тело ответа", true, response.path("ok"));

        //получаем id курьера, чтобы удалить запись после теста
        id = courierClient.getCourierId(credsFrom(courier));
    }

    @After
    public void tearsDown() {
        courierClient.deleteCourier(id);
    }

}
