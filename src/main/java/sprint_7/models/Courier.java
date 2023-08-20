package sprint_7.models;

public class Courier {
    private String login;
    private String password;
    private String firstName;

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    public Courier withLogin(String login) {
        this.login = login;
        return this;
    }

    public Courier withPassword(String password) {
        this.password = password;
        return this;
    }

    public Courier withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }


}
