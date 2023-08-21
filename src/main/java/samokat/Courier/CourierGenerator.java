package samokat.Courier;

import samokat.models.Courier;

import static samokat.Utils.randomString;

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
