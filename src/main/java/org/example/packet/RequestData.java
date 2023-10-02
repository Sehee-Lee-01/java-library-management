package org.example.packet;

public class RequestData { //** 논리적인 결합, 분리,
    // ** 컴파일 에러에서 잡히지 않아서(생성자) ex) 등록용 리퀘스트
    public int id; // 5000 미만
    public String name; // 100자 미만
    public String author; // 100자 미만
    public int pages; // 5000 미만

    public RequestData() {
    } // 전체 조회

    public RequestData(int id) {
        this.id = id;
    }

    public RequestData(String name) {
        this.name = name;
    }

    public RequestData(String name, String author, int pages) {
        this.name = name;
        this.author = author;
        this.pages = pages;
    }
}
