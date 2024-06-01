package swagger.api.demo.model;


public class LoginRequest {
    private LoginPayload payload;

    public LoginPayload getPayload() {
        return payload;
    }

    public void setPayload(LoginPayload payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "payload=" + payload +
                '}';
    }
}
