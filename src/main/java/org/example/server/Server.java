package org.example.server;

import org.example.type.ModeType;
import org.example.connect.Request;
import org.example.server.controller.BookController;
import org.example.server.service.BookService;

// 싱글톤으로 생성
// 콘솔에서 입력 받은 모드, 메뉴, 각 메뉴에 대한 입력 값들을 세팅.
// 모드 = 레포 세팅
// 메뉴 = 컨트롤러 뭐할지 세팅
public class Server {
    private Server() {
    }

    private static BookController bookController;

    public static void setModeType(ModeType modeType) {
        BookService bookService = new BookService(modeType.getRepository());
        bookController = new BookController(bookService);
    }

    public static String request(Request request) {
        switch (request.menuType) {
            case REGISTER -> {
                return bookController.register(request.requestData.requestBookDto);
            }
            case READ_ALL -> {
                return bookController.readAll();
            }
            case SEARCH_BY_NAME -> {
                return bookController.seachByName(request.requestData.bookName);
            }
            case BORROW -> {
                return bookController.borrow(request.requestData.bookId);
            }
            case RETURN -> {
                return bookController.restore(request.requestData.bookId);
            }
            case LOST -> {
                return bookController.lost(request.requestData.bookId);
            }
            case DELETE -> {
                return bookController.delete(request.requestData.bookId);
            }
        }
        return null;// 예외 처리?
    }
}