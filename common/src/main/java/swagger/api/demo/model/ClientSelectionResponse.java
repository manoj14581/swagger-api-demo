package swagger.api.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ClientSelectionResponse {
    private String token;
    private String tokenSubject;
    private String applicationState;
    private String createdTime;
    private String expiryTime;
    private String isMobile;
    private String refreshToken;

    @JsonProperty("isAccountMirroringSessionInProgress")
    private boolean isAccountMirroringSessionInProgress;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenSubject() {
        return tokenSubject;
    }

    public void setTokenSubject(String tokenSubject) {
        this.tokenSubject = tokenSubject;
    }

    public String getApplicationState() {
        return applicationState;
    }

    public void setApplicationState(String applicationState) {
        this.applicationState = applicationState;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getIsMobile() {
        return isMobile;
    }

    public void setIsMobile(String isMobile) {
        this.isMobile = isMobile;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean getIsAccountMirroringSessionInProgress() {
        return isAccountMirroringSessionInProgress;
    }

    public void setIsAccountMirroringSessionInProgress(boolean accountMirroringSessionInProgress) {
        this.isAccountMirroringSessionInProgress = accountMirroringSessionInProgress;
    }

    @Override
    public String toString() {
        return "ClientSelectionResponse{" +
                "token='" + token + '\'' +
                ", tokenSubject='" + tokenSubject + '\'' +
                ", applicationState='" + applicationState + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", expiryTime='" + expiryTime + '\'' +
                ", isMobile='" + isMobile + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", isAccountMirroringSessionInProgress='" + isAccountMirroringSessionInProgress + '\'' +
                '}';
    }
}
