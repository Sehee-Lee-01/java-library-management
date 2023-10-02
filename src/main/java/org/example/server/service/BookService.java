package org.example.server.service;

import org.example.server.entity.Book;
import org.example.server.entity.BookState;
import org.example.server.repository.Repository;

import java.util.Calendar;
import java.util.Date;

/* Q. 조건이 맞지 않아 요청이 실패할 경우, ServerException이라는 커스텀 예외 안에 메시지를 넣고 던지는 로직을 구현했습니다.
    + 도메인에 대한 예외를 나타내는 것, 북 예외에서 북 에러코드(enum)를 가져가는 것
    + 도메인에 대한 예외를 한 곳에서 관리하면 좋지 않을까?
    위 방식으로하면 if문보다는 깔끔해지는 것 같지만 예외를 일부로 직접 만들어서 던지는 것이 조금은 안정적이지 않은 것 같다는 생각이 들기도 합니다.
    멘토님들은 어떻게 생각하시는지 궁금합니다! */

public class BookService implements Service {
    private final Repository repository;

    public BookService(Repository repository) {
        this.repository = repository;
    }

    public void register(String name, String author, int pages) {
        Book book = new Book(name, author, pages);
        repository.create(book);
    }

    public String readAll() {
        return repository.readAll();
    }

    public String searchByName(String bookName) {
        return repository.searchByName(bookName);
    }

    public void borrow(int bookId) {
        Book book = repository.getById(bookId);
        book.borrow(); // 상태 추가가 되었을 때 서비스에 대한 의존이 사라진다.
    }

    public void restore(int bookId) {
        Book book = repository.getById(bookId);
        BookState bookState = BookState.valueOf(book.state);
        if (bookState.equals(BookState.CAN_BORROW) || bookState.equals(BookState.LOADING))
            throw bookState.throwStatusException();
        book.state = BookState.LOADING.name();
        Date curr = new Date();
        Book.calendar.setTime(curr);
        Book.calendar.add(Calendar.MINUTE, 5); // **java8 시간 api
        book.endLoadTime = Book.format.format(new Date(Book.calendar.getTimeInMillis()));
    }

    public void lost(int bookId) {
        Book book = repository.getById(bookId);
        BookState bookState = BookState.valueOf(book.state);
        if (bookState.equals(BookState.LOST))
            throw bookState.throwStatusException();
        if (bookState.equals(BookState.LOADING)) {
            book.endLoadTime = "";
        }
        book.state = BookState.LOST.name();
    }

    public void delete(int bookId) {
        repository.getById(bookId);
        repository.delete(bookId);
    }
}
