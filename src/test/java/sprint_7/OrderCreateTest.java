package sprint_7;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sprint_7.order.OrderClient;

import java.io.File;

import static org.hamcrest.CoreMatchers.notNullValue;


@RunWith(Parameterized.class)
public class OrderCreateTest {


    @Parameterized.Parameter
    public String path;
    private OrderClient orderClient;

    @Parameterized.Parameters(name = "Color {0}")
    public static Object[][] params() {
        return new Object[][]{
                {"src/test/resources/newOrderWithoutColor.json"},
                {"src/test/resources/newOrderWithColorBlack.json"},
                {"src/test/resources/newOrderWithColorGrey.json"},
                {"src/test/resources/newOrderWithColorBlackGrey.json"}
        };
    }

    @Before
    public void SetUp() {
        orderClient = new OrderClient();
    }

    //создаем заказ
    @Test
    public void CreateOrder() {

        File json = new File(path);
        Response response = orderClient.create(json);
        Assert.assertEquals("Неверный статус код", HttpStatus.SC_CREATED, response.statusCode());
    }

    //проверка, что тело ответа возвращает track
    @Test
    public void CreateOrderReturnTrack() {
        File json = new File(path);
        Response response = orderClient.create(json);
        int track = response.path("track");
        response.then().assertThat().body("track", notNullValue());
    }
}
