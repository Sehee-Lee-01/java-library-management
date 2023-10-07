package org.example.server.service;

import org.example.packet.BookRegisterDto;
import org.example.packet.BookResponseDto;
import org.example.server.entity.Book;
import org.example.server.repository.Repository;

import java.util.LinkedList;

public class BookService implements Service {
    private final Repository repository;

    public BookService(Repository repository) {
        this.repository = repository;
    }

    public void register(BookRegisterDto bookDto) {
        repository.save(new Book(bookDto));
    }

    public LinkedList<BookResponseDto> readAll() {
        LinkedList<BookResponseDto> bookDtos = new LinkedList<>();
        repository.findAll().forEach(book -> {
            bookDtos.add(new BookResponseDto(book));
        });
        return bookDtos;
    }

    public LinkedList<BookResponseDto> searchAllByName(String name) {
        LinkedList<BookResponseDto> bookDtos = new LinkedList<>();
        repository.findAllByName(name).forEach(book -> {
            bookDtos.add(new BookResponseDto(book));
        });
        return bookDtos;
    }

    public void borrow(int bookId) {
        Book book = repository.getById(bookId);
        book.borrow();
    }

    public void restore(int bookId) {
        Book book = repository.getById(bookId);
        book.restore();
    }

    public void lost(int bookId) {
        Book book = repository.getById(bookId);
        book.lost();
    }

    public void delete(int bookId) {
        repository.getById(bookId);
        repository.delete(bookId);
    }
}
