package mydist.mydist.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Blessing.Ekundayo on 11/25/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadMastersResponse {

    @JsonProperty("status")
    public Status status;


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

        public boolean isSuccess() {
            return statusCode.equals(ResponseCodes.SUCCESS_CODE);
        }
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}