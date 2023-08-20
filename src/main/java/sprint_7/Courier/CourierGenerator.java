package sprint_7.Courier;

import sprint_7.models.Courier;

import static sprint_7.Utils.randomString;

public class CourierGenerator {

    public static Courier randomCourier() {
        return new Courier()
                .withLogin(randomString(8))
                .withPassword(randomString(10))
                .withFirstName(randomString(17));
    }

   public static Courier courierWithoutPassword() {
        return new Courier()
                .withLogin(randomString(8))
                .withFirstName(randomString(10));
    }
}
