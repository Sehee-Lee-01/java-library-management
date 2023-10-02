package org.example.server.service;

public interface Service {
    void register(String name, String author, int pages);

    String readAll(); // ** String 반환

    String searchByName(String bookName); // ** String 반환

    void borrow(int bookId);

    void restore(int bookId);

    void lost(int bookId);

    void delete(int bookId);
}
