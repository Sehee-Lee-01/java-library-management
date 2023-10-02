package org.example.server.repository;

import org.example.server.entity.Book;
import org.example.server.exception.BookNotFoundException;
import org.example.server.exception.EmptyLibraryException;

import java.util.LinkedHashMap;
import java.util.Optional;

public class InMemoryRepository implements Repository {
    public int newId = 1; //  생성 예정인 id 값, 1부터 생성
    public final LinkedHashMap<Integer, Book> data = new LinkedHashMap<>();

    // + 멀티 스레드 상황이라면? atomic 시리즈, 동시성 문제, concurrent hashmap
    @Override
    public void create(Book book) {
        int bookId = newId++;
        book.id = bookId;
        data.put(bookId, book);
    }

    @Override
    public String readAll() {
        if (data.isEmpty()) // ** emptylsit를 보내는것이 낫지않을까?
            throw new EmptyLibraryException();
        StringBuilder sb = new StringBuilder();
        data.values().forEach((book) -> {
            sb.append(checkLoadTime(book));
        });
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String searchByName(String bookName) {
        StringBuilder sb = new StringBuilder();
        data.values().forEach(
                book -> {
                    checkLoadTime(book); // 
                    if (book.name.contains(bookName)) sb.append(book);
                }
        );
        if (sb.isEmpty())
            throw new EmptyLibraryException(); // + 네이밍
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public Book getById(int bookId) {
        Optional<Book> book = Optional.ofNullable(data.get(bookId));
        if (book.isEmpty())
            throw new BookNotFoundException(); // + 네이밍
        return checkLoadTime(book.get());
    }

    @Override
    public void delete(int bookId) {
        data.remove(bookId);
    }

    @Override
    public void save() { //** 제거
    }
}
