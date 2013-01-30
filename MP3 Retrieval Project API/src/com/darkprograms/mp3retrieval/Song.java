package com.darkprograms.mp3retrieval;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 11/7/12
 * Time: 7:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class Song {

    private String url;
    private String response;

    public Song(String url, String response){
        setUrl(url);
        setResponse(response);
    }

    public String getUrl() {
        return url;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    public String getResponse() {
        return response;
    }

    private void setResponse(String response) {
        this.response = response;
    }
}
