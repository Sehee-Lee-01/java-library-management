package org.example.server.repository;

import org.example.server.entity.Book;
import org.example.server.entity.BookState;

import java.text.ParseException;
import java.util.Date;

/* Q. 이번에 구현한 것(반납 후 5분동안 도서 정리 기능)은 스레드 사용을 생각하려다가 시간 부족, 실력 부족ㅠㅠ,
    중간에 프로그램이 꺼지면 엡데이트 해야하는 데이터 목록의 누락 등의 문제로 스레드 같은 기능을 사용하지 않고
    도서 정리 완료 예정 시간(반납 후 5분)을 미리 저장하고, 참고할 때마다 현재 시간과 비교하는 형식의 로직으로 구현했습니다.
    실제 스프링에서는 따로 사용하는 라이브러리가 있는지,
    아니면 스레드를 직접 생성하여 관리하는 것인지 이런 비동기 작업을 관리하는지 궁금합니다!
    + 스케줄러, 이벤트 /(스프링이 아니라면) 스레드
    그리고 실제 스프링을 사용하는 서버에서는 중간에 서버가 오류로 인해 중단될 경우, 일정 시간이 지난 후 업데이트 되는 데이터들은 어떻게 처리를 하는지도 궁금합니다.
    + 반납이라는 상태 저장, 정리중에 에러 -> 보정 처리, 스케쥴링으로 체크,,방법을 생각해보자!
 */
public interface Repository {
    default Book checkLoadTime(Book book) { // ** 레포지토리에서 구현해야할까? 레포지토리의 역할
        if (BookState.valueOf(book.state).equals(BookState.LOADING)) {
            try {
                Date endLoadTime = Book.format.parse(book.endLoadTime);
                Date now = Book.format.parse(Book.format.format(new Date()));
                if (now.after(endLoadTime)) {
                    book.state = BookState.CAN_BORROW.name();
                    book.endLoadTime = "";
                    return book;
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return book;
    }

    void create(Book book); // + save

    String readAll(); // ** String 반환, 뷰에 종속적이다. controller에서 하는 것

    String searchByName(String bookName);// ** String 반환, 뷰에 종속적이다. controller에서 하는 것

    Book getById(int bookId); // + null 미허용/ find null 허용

    void delete(int bookId);

    void save(); // 구현체 노출시킬 수도 있다. 논리적 결합. 파일 저장하는 것
}
