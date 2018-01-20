package mydist.mydist.models.push;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Blessing.Ekundayo on 1/17/2018.
 */

public class MasterPush {
    @JsonProperty("repId")
    String repId;
    @JsonProperty("username")
    String username;
    @JsonProperty("repCode")
    String repCode;
    @JsonProperty("new_retailers")
    List<NewRetailerPush> newRetailers;
    @JsonProperty("collections")
    List<CollectionPush> collections;
    @JsonProperty("coverage")
    List<Coverage> coverage;

    public MasterPush(String repId, String username, String repCode, List<NewRetailerPush> newRetailers, List<CollectionPush> collections, List<Coverage> coverage) {
        this.repId = repId;
        this.username = username;
        this.repCode = repCode;
        this.newRetailers = newRetailers;
        this.collections = collections;
        this.coverage = coverage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public List<NewRetailerPush> getNewRetailers() {
        return newRetailers;
    }

    public void setNewRetailers(List<NewRetailerPush> newRetailers) {
        this.newRetailers = newRetailers;
    }

    public List<CollectionPush> getCollections() {
        return collections;
    }

    public void setCollections(List<CollectionPush> collections) {
        this.collections = collections;
    }

    public List<Coverage> getCoverage() {
        return coverage;
    }

    public void setCoverage(List<Coverage> coverage) {
        this.coverage = coverage;
    }
}
