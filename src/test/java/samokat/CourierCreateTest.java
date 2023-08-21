package samokat;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import samokat.Courier.CourierClient;
import samokat.models.Courier;

import static org.junit.Assert.assertEquals;
import static samokat.Courier.CourierGenerator.courierWithoutPassword;
import static samokat.Courier.CourierGenerator.randomCourier;
import static samokat.models.CourierCreds.credsFrom;

public class CourierCreateTest {

    private int id;
    private String expectedBodyAnswer;
    private CourierClient courierClient;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    //тестируем создание курьера
    @Test
    @DisplayName("createCourierTest")
    public void createCourierTest() {

        Courier courier = randomCourier();
        Response response = courierClient.create(courier);

        //получаем id курьера, чтобы удалить запись после теста
        id = courierClient.getCourierId(credsFrom(courier));

        assertEquals("Неверный статус код", HttpStatus.SC_CREATED, response.statusCode());
    }

    //проверка, что нельзя создать двух одинаковых курьеров
    @Test
    @DisplayName("createTwoCouriersTest")
    public void createTwoCouriersTest() {
        Courier courier = randomCourier();
        Response response = courierClient.create(courier);//создали одного курьера
        //получаем id курьера, чтобы удалить запись после теста
        id = courierClient.getCourierId(credsFrom(courier));

        assertEquals(HttpStatus.SC_CREATED, response.statusCode());//проверили, что курьер создан
        Response response_other = courierClient.create(courier);//создаем еще одного курьера с теми же логином и паролем
        //получаем тело ответа
        expectedBodyAnswer = response_other.path("message");

        assertEquals("Создались две одинаковые записи", HttpStatus.SC_CONFLICT, response_other.statusCode());//проверяем, что произошел конфликт
        assertEquals("Этот логин уже используется", expectedBodyAnswer);
    }

    //проверка, что нельзя создать курьера с одним и тем же логином
    @Test
    @DisplayName("createCourierWithTheSameLogin")
    public void createCourierWithTheSameLogin() {
        Courier courier = randomCourier();
        Response response = courierClient.create(courier);
        //создаем второго курьера с тем же логином и другими паролем и именем
        Courier courierSameLogin = new Courier().withLogin(courier.getLogin()).withPassword("32323").withFirstName("ehehe");
        Response responseSameLogin = courierClient.create(courierSameLogin);
        //получаем id курьера, чтобы удалить запись после теста
        id = courierClient.getCourierId(credsFrom(courier));
        //получаем тело ответа
        expectedBodyAnswer = responseSameLogin.path("message");

        assertEquals("Создались два курьера с одним и тем же логином", HttpStatus.SC_CONFLICT, responseSameLogin.statusCode());
        assertEquals("Этот логин уже используется", expectedBodyAnswer);
    }

    //проверка, что нельзя создать курьера, если не все поля переданы в запрос
    @Test
    @DisplayName("createCourierNoPasswordTest")
    public void createCourierNoPasswordTest() {
        Courier courier = courierWithoutPassword();
        Response response = courierClient.create(courier);
       //получаем тело ответа
        expectedBodyAnswer = response.path("message");
        assertEquals("Создался курьер, без пароля", HttpStatus.SC_BAD_REQUEST, response.statusCode());
        assertEquals("Недостаточно данных для создания учетной записи", expectedBodyAnswer);
    }

    //проверка, что код ответа при создании курьера ok:true
    @Test
    @DisplayName("createCourierAndCheckAnswerTrueTest")
    public void createCourierAndCheckAnswerTrueTest() {
        Courier courier = randomCourier();
        Response response = courierClient.create(courier);
        //получаем id курьера, чтобы удалить запись после теста
        id = courierClient.getCourierId(credsFrom(courier));

        assertEquals("Получили неожидаемое тело ответа", true, response.path("ok"));
    }

    @After
    public void tearsDown() {
        courierClient.deleteCourier(id);
    }
}
