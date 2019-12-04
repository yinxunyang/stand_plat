package com.bestvike.portal.support;

import java.io.Serializable;
import java.util.List;

public class RolePermission implements Serializable {
    private String route;
    private List<String> operates;
    private List<String> urls;

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public List<String> getOperates() {
        return operates;
    }

    public void setOperates(List<String> operates) {
        this.operates = operates;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
