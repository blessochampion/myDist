package mydist.mydist.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Blessing.Ekundayo on 11/9/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationResponse {
    @JsonProperty("status")
    public Status status;
    @JsonProperty("salesrep")
    public User user;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public class Status {
        @JsonProperty("status_code")
        public String statusCode;
        @JsonProperty("status_message")
        public String message;

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isSuccess(){
            return statusCode.equals(ResponseCodes.SUCCESS_CODE);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static  class User {
        String repId;
        String repCode;
        String userName;
        String fullName;

        public String getRepId() {
            return repId;
        }

        public void setRepId(String repId) {
            this.repId = repId;
        }

        public String getRepCode() {
            return repCode;
        }

        public void setRepCode(String repCode) {
            this.repCode = repCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
    }

}
