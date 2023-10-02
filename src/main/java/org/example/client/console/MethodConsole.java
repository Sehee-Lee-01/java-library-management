package org.example.client.console;

import org.example.client.io.IO;
import org.example.packet.Request;
import org.example.packet.RequestData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MethodConsole {
    public enum MethodType {
        REGISTER(1, "1. 도서 등록", "\n[System] 도서 등록 메뉴로 넘어갑니다.\n", new ArrayList<>(Arrays.asList(
                ("Q. 등록할 도서 제목을 입력하세요.\n\n> "),
                ("Q. 작가 이름을 입력하세요.\n\n> "),
                ("Q. 페이지 수를 입력하세요.\n\n> ")
        )), MethodConsole::scanAndSetBookInfo),
        READ_ALL(2, "2. 전체 도서 목록 조회", "\n[System] 전체 도서 목록입니다.\n", new ArrayList<>(), (io -> new RequestData())),
        SEARCH_BY_NAME(3, "3. 제목으로 도서 검색", "\n[System] 제목으로 도서 검색 메뉴로 넘어갑니다.\n\n", new ArrayList<>(Arrays.asList("Q. 검색할 도서 제목 일부를 입력하세요.\n\n> ")), MethodConsole::scanAndSetBookName),
        BORROW(4, "4. 도서 대여", "\n[System] 도서 대여 메뉴로 넘어갑니다.\n", new ArrayList<>(Arrays.asList("Q. 대여할 도서번호를 입력하세요\n\n> ")), MethodConsole::scanAndSetBookId),
        RESTORE(5, "5. 도서 반납", "\n[System] 도서 반납 메뉴로 넘어갑니다.\n", new ArrayList<>(Arrays.asList("Q. 반납할 도서번호를 입력하세요\n\n> ")), MethodConsole::scanAndSetBookId),
        LOST(6, "6. 도서 분실", "\n[System] 도서 분실 처리 메뉴로 넘어갑니다.\n", new ArrayList<>(Arrays.asList("Q. 분실 처리할 도서번호를 입력하세요\n\n> ")), MethodConsole::scanAndSetBookId),
        DELETE(7, "7. 도서 삭제", "\n[System] 도서 삭제 처리 메뉴로 넘어갑니다.\n", new ArrayList<>(Arrays.asList("Q. 삭제 처리할 도서번호를 입력하세요\n\n> ")), MethodConsole::scanAndSetBookId);
        private static final Map<Integer, MethodType> BY_NUMBER =
                Stream.of(values()).collect(Collectors.toMap(MethodType::getNum, Function.identity()));

        public static MethodType valueOfNumber(int num) {
            return BY_NUMBER.get(num);
        }

        public static final String MENU_CONSOLE = "Q. 사용할 기능을 선택해주세요.\n"
                + String.join("", Stream.of(values()).map(type -> type.name + "\n").toArray(String[]::new)) + "\n> ";

        private final int num;
        private final String name;
        public final String alert;
        private final ArrayList<String> questions;
        private final Function<IO, RequestData> scanInfoFunction;

        MethodType(int num, String name, String alert, ArrayList<String> questions, Function<IO, RequestData> scanInfoFunction) {
            this.num = num;
            this.name = name;
            this.alert = alert;
            this.questions = questions;
            this.scanInfoFunction = scanInfoFunction;
        }

        public int getNum() {
            return num;
        }

        public ArrayList<String> getQuestions() {
            return questions;
        } // ** 논리적인 결합

        public String getQuestion() {
            return questions.get(0);
        } // ** 논리적인 결합

        public RequestData scanInfo(IO io) {
            return this.scanInfoFunction.apply(io);
        }
    }

    private static MethodType clientMethod; //** 없어져도 되지않을까?

    private MethodConsole() {
    }

    // ** scanTypeAndInfo, setClientMethod 기능 분리, 캡슐화!
    public static Request scanTypeAndInfo(IO io) {
        Request request = setClientMethod(io);
        try {
            request.requestData = clientMethod.scanInfo(io);
        } catch (ValidateException e) {
            io.println(e.getMessage());
            request.requestData = clientMethod.scanInfo(io);
        }
        return request;
    }

    public static Request setClientMethod(IO io) { // ** 상태노출, 노출시키지 않는 방향이나 , 분리하는 방향으로!
        io.print(MethodType.MENU_CONSOLE);
        int selectNum = Validator.validateSelectNum(MethodType.values().length, io.scanLine());
        clientMethod = MethodType.valueOfNumber(selectNum);
        io.println(clientMethod.alert);
        return new Request(clientMethod.name());
    }

    // ** 주석은 최대한 간결하게, 메서드 명에서 정보를 담을 수 있게.
    public static RequestData scanAndSetBookInfo(IO io) {
        String[] bookInfo = clientMethod.getQuestions().stream().map(question -> {
            try {
                io.print(question);
                return Validator.validateNameAndAuthor(io.scanLine());
            } catch (ValidateException e) {
                io.println(e.getMessage());
                io.print(question);
                return Validator.validateNameAndAuthor(io.scanLine());
            }
        }).toArray(String[]::new); // ** 객체로 넘기는 방법
        return Validator.validateBook(bookInfo);
    }

    public static RequestData scanAndSetBookName(IO io) { // ** 파라미터에 clientMethod를 주면되지 않을까? 분리해서 하면!
        RequestData requestData = new RequestData(); // ** 수정, 제거
        io.print(clientMethod.getQuestion());
        String name = Validator.validateNameAndAuthor(io.scanLine());
        return new RequestData(name);
    }

    public static RequestData scanAndSetBookId(IO io) {
        io.print(clientMethod.getQuestion());
        int id = Validator.validateIdAndPages(io.scanLine());
        return new RequestData(id);
    }
}
