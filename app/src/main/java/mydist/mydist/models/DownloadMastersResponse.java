package mydist.mydist.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Blessing.Ekundayo on 11/25/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DownloadMastersResponse {
    @JsonProperty("status_code")
    public String statusCode;
    @JsonProperty("status_message")
    public String message;
    @JsonProperty("status")
    public Status status;
    @JsonProperty("master")
    public Master master;
    @JsonProperty("salesrep")
    public AuthenticationResponse.User user;
    public DownloadMastersResponse() {
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

        public boolean isSuccess() {
            return statusCode.equals(ResponseCodes.SUCCESS_CODE);
        }
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Master {

        @JsonProperty("channels")
        public List<Channel> channels;
        @JsonProperty("subchannels")
        public List<SubChannel> subChannels;
        @JsonProperty("brands")
        public List<Brand> brands;
        @JsonProperty("products")
        public List<Product> products;
        /*coverage*/
        @JsonProperty("merchandizingList")
        public List<Merchandize> merchandizingList;


        public List<Channel> getChannels() {
            return channels;
        }

        public void setChannels(List<Channel> channels) {
            this.channels = channels;
        }

        public List<SubChannel> getSubChannels() {
            return subChannels;
        }

        public void setSubChannels(List<SubChannel> subChannels) {
            this.subChannels = subChannels;
        }

        public List<Brand> getBrands() {
            return brands;
        }

        public void setBrands(List<Brand> brands) {
            this.brands = brands;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }

        public List<Merchandize> getMerchandizingList() {
            return merchandizingList;
        }

        public void setMerchandizingList(List<Merchandize> merchandizingList) {
            this.merchandizingList = merchandizingList;
        }


    }


    public void setUser(AuthenticationResponse.User user) {
        this.user = user;
    }

    public AuthenticationResponse.User getUser() {
        return user;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }
}
