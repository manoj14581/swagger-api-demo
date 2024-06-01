package swagger.api.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class LoginPayload {
    private String username = "manager@360logica.com";
    private String password = "Welc0me19ALPHA-Q@";
    private boolean stayLoggedIn = true;
    private String actor = "eyJyZWZlcnJlciI6Ik1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDEwLjA7IFdpbjY0OyB4NjQpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS84MC4wLjM5ODcuMTQ5IFNhZmFyaS81MzcuMzYifQ==";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStayLoggedIn() {
        return stayLoggedIn;
    }

    public void setStayLoggedIn(boolean stayLoggedIn) {
        this.stayLoggedIn = stayLoggedIn;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    @Override
    public String toString() {
        return "LoginPayload{" +
                "username='" + username + '\'' +
                ", password='**********'" +
                ", stayLoggedIn=" + stayLoggedIn +
                ", actor='" + actor + '\'' +
                '}';
    }
}
