package com.hello.apiserver.api.util.vo;

import java.util.Date;

public class HttpResponseVo {
    private String response = "httpreponse";
    private long timestamp = new Date().getTime();
    private int status;
    private String result = "";
    private String message = "";
    private String path = "";

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setHttpResponse(String msg, int status, String result) {
        this.message = msg;
        this.status = status;
        this.result = result;
    }
}
