package br.edu.ifsp.dmo.sitesinteressantes.model;


import java.util.Objects;

public class Site {

    private String title;
    private String url;
    private TagSite tag;

    public Site(String title, String url) {
        this(title, url, null);
    }

    public Site(String title, String url, TagSite tag) {
        this.title = title;
        this.url = url;
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TagSite getTag() {
        return tag;
    }

    public void setTag(TagSite tag) {
        this.tag = tag;
    }

}
