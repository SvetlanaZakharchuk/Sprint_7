package samokat.models;

public class CourierCreds {
    private String login;
    private String password;

    public CourierCreds(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CourierCreds(String login) {
        this.login = login;
    }

    //получаем логин и пароль созданного курьера
    public static CourierCreds credsFrom(Courier courier) {
        return new CourierCreds(courier.getLogin(), courier.getPassword());
    }

    //получаем только пароль, чтобы проверить авторизацию без логина
    public static CourierCreds credsFromPassword(Courier courier) {
        return new CourierCreds(null, courier.getPassword());
    }
}
