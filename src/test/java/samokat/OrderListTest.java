package samokat;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import samokat.order.OrderClient;

import static org.junit.Assert.assertEquals;

public class OrderListTest {
    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    //получаем список заказов
    @Test
    @DisplayName("getOrdersListTest")
    public void getOrdersListTest() {
        Response response = orderClient.getOrdersList();

        assertEquals("Не получен список заказов", HttpStatus.SC_OK, response.statusCode());
    }
}
