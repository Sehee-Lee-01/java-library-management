package org.example.server.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/* Q. 날짜는 보통은 DB에 저장할 때 어떤 형식으로 저장하는지 궁금합니다!
+ 나중에 고민해보기, mysql datetime(6), timestamp 회사마다 다르다.
*/

public class Book {
    public static Calendar calendar = Calendar.getInstance(); // ** 제거, 의존성, localdatetime 타입(java8 time api)
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // ** 제거, 의존성, localdatetime 타입
    public int id; // 5000 미만
    public String name; // 100자 미만
    public String author; // 100자 미만
    public int pages; // 5000 미만
    public String state;//**구현에 의해서 도메인이 변경된 사례, 도메인->비즈니스 해결에 필요한 정보만 담고 있어야 한다. Bookstate, 데이터 저장
    // ** state 안에 endLoadTime을 특정 state 안에 넣어준다.
    public String endLoadTime; // ** 특정 상태에서만 가지고 있는 값, 특정상태에 종속 되어있다. 입고중이라느 상태가 더 추가된다면? -> 더 복잡해진다.


    public Book(String name, String author, int pages) {
        this.name = name;
        this.author = author;
        this.pages = pages;
        this.state = BookState.CAN_BORROW.name();
        this.endLoadTime = "";
    }

    public Book(int id, String name, String author, int pages, String state, String borrowTime) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.pages = pages;
        this.state = state;
        this.endLoadTime = borrowTime;
    }

    @Override
    public String toString() { // 뷰에서 출력이 변경되어야 한다면? 뷰애의한 변경이 도메인을 변경할 수 잇다.dto를 통해 내보내고, 뷰에서 관리해준다면?(의존성)
        return "\n도서번호 : " + id + "\n" +
                "제목 : " + name + "\n"
                + "작가 이름 : " + author + "\n"
                + "페이지 수 : " + pages + " 페이지\n" +
                "상태 : " + BookState.valueOf(state).getStatus() + "\n" +
                "도서 정리 완료 예정 시간 : " + endLoadTime + "\n" + // 테스트용
                "\n------------------------------\n";
    }

//    public void borrow() {
//        if (state.equals(BookState.BORROWED) || state.equals(BookState.LOADING) || state.equals(BookState.LOST))
//            // ** 입고중이라는 상태가 추가된다면? bookState, bookservice도 바뀌어야 된다.(상태와 행위가 분리된다.)
//            // ** 북한테 직접 질의하는 것이 좋지 않을까?
//            throw state.throwStatusException(); // ** book borrow exception
//        state = BookState.BORROWED.name();
//    }
}
