package org.example.client.console;

import org.example.client.io.ConsoleIO;
import org.example.client.io.IO;
import org.example.packet.BookDto;
import org.example.packet.responsePacket.ResponseFailWithMessage;
import org.example.packet.responsePacket.ResponsePacket;
import org.example.packet.responsePacket.ResponseSuccessWithData;
import org.example.packet.responsePacket.ResponseSuccessWithNoData;

import java.util.LinkedList;

public class ResponsePrinter {
    private final IO IO = ConsoleIO.getInstance();

    public void printResponse(ResponsePacket responsePacket) {
        MethodType methodType = MethodType.valueOf(responsePacket.METHOD);
        if (responsePacket instanceof ResponseSuccessWithData) {
            LinkedList<BookDto> bookDtos = ((ResponseSuccessWithData) responsePacket).BOOKS;
            if (bookDtos.isEmpty()) {
                IO.print(System.lineSeparator() + "[System] 존재하는 도서가 없습니다." + System.lineSeparator());
            } else {
                IO.println(booksToString(bookDtos));
                IO.println(methodType.SUCCESS_MESSAGE);
            }
            return;
        }
        if (responsePacket instanceof ResponseSuccessWithNoData) {
            IO.println(methodType.SUCCESS_MESSAGE);
            return;
        }
        if (responsePacket instanceof ResponseFailWithMessage) {
            String failMessage = ((ResponseFailWithMessage) responsePacket).FAIL_MESSAGE;
            IO.println(failMessage);
        }
    }

    private String booksToString(LinkedList<BookDto> bookDtos) {
        StringBuffer sb = new StringBuffer();
        bookDtos.forEach(
                bookDto -> {
                    sb.append(System.lineSeparator()).append("도서번호 : ").append(bookDto.id).append(System.lineSeparator()).append("제목 : ").append(bookDto.NAME).append(System.lineSeparator()).append("작가 이름 : ").append(bookDto.AUTHOR).append(System.lineSeparator()).append("페이지 수 : ").append(bookDto.PAGES).append(" 페이지").append(System.lineSeparator()).append("상태 : ").append(bookDto.status).append(System.lineSeparator()).append(System.lineSeparator()).append("------------------------------").append(System.lineSeparator());
                }
        );
        return sb.toString();
    }
}