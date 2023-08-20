package sprint_7;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import sprint_7.order.OrderClient;

import static org.junit.Assert.assertEquals;

public class OrderListTest {
    private OrderClient orderClient;

    @Before
    public void SetUp() {
        orderClient = new OrderClient();
    }

    //получаем список заказов
    @Test
    public void GetOrdersListTest() {
        Response response = orderClient.getOrdersList();
        assertEquals("Не получен список заказов", HttpStatus.SC_OK, response.statusCode());
    }
}
