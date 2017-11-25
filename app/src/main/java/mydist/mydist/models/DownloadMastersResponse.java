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
    static class Master {

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

        static class Channel {
            @JsonProperty("channelId")
            public String channelId;
            @JsonProperty("channelName")
            public String channelName;

            public Channel() {
                super();
            }

            public String getChannelId() {
                return channelId;
            }

            public void setChannelId(String channelId) {
                this.channelId = channelId;
            }

            public String getChannelName() {
                return channelName;
            }

            public void setChannelName(String channelName) {
                this.channelName = channelName;
            }
        }

        static class SubChannel {
            @JsonProperty("subChannelId")
            public String subChannelId;
            @JsonProperty("subChannelName")
            public String subChannelName;


            public SubChannel() {
            }

            public String getChannelId() {
                return subChannelId;
            }

            public void setChannelId(String subChannelId) {
                this.subChannelId = subChannelId;
            }

            public String getChannelName() {
                return subChannelName;
            }

            public void setChannelName(String subchannelName) {
                this.subChannelName = subchannelName;
            }
        }

        static class Brand {
            @JsonProperty("brandId")
            public String brandId;
            @JsonProperty("brandName")
            public String brandName;

            public Brand() {
            }

            public String getBrandId() {
                return brandId;
            }

            public void setBrandId(String brandId) {
                this.brandId = brandId;
            }

            public String getBrandName() {
                return brandName;
            }

            public void setBrandName(String brandName) {
                this.brandName = brandName;
            }
        }

        static class Product {
            @JsonProperty("productId")
            public String productId;
            @JsonProperty("productName")
            public String productName;
            @JsonProperty("casePrice")
            public String casePrice;
            @JsonProperty("piecePrice")
            public String piecePrice;
            @JsonProperty("brandId")
            public String brandId;

            public Product() {
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getCasePrice() {
                return casePrice;
            }

            public void setCasePrice(String casePrice) {
                this.casePrice = casePrice;
            }

            public String getPiecePrice() {
                return piecePrice;
            }

            public void setPiecePrice(String piecePrice) {
                this.piecePrice = piecePrice;
            }

            public String getBrandId() {
                return brandId;
            }

            public void setBrandId(String brandId) {
                this.brandId = brandId;
            }
        }

        static class Merchandize {
            @JsonProperty("brandName")
            public String brandName;
            @JsonProperty("merchItem")
            public String merchandizeItem;

            public Merchandize() {
            }

            public String getBrandName() {
                return brandName;
            }

            public void setBrandName(String brandName) {
                this.brandName = brandName;
            }

            public String getMerchandizeItem() {
                return merchandizeItem;
            }

            public void setMerchandizeItem(String merchandizeItem) {
                this.merchandizeItem = merchandizeItem;
            }
        }
    }


    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }
}
