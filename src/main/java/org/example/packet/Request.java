package org.example.packet;

//**어떻게 교환할지 다시한번 생각
public class Request {
    public String method;
    public RequestData requestData;

    public Request(String method) {
        this.method = method;
    }

    public Request(String method, RequestData requestData) {
        this.method = method;
        this.requestData = requestData;
    }
}
