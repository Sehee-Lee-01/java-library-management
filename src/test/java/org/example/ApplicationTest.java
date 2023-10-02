package org.example;

import org.example.client.console.MethodConsole;
import org.example.client.console.ModeConsole;
import org.example.packet.Request;
import org.example.packet.RequestData;
import org.example.server.Server;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationTest {
    // 아래 테스트는 end to end test
    // ** 도메인 비즈니스 로직 테스트를 수행하는 것이 중요하다. 단위테스트, 통합테스트
    // ** -> 이번에는 단위테스트 중심으로 book,repo,service 객체간의 상호작용 테스트
    @Test
    @DisplayName("애플리케이션 생성 테스트")
    void create() {
        Application application = new Application();
        assertNotNull(application);
    }

    @Test
    @DisplayName("애플리케이션 중단(예외 발생) 후 데이터 저장 확인")
    void saveData() {
        Application application = new Application();
        Server.setServer(ModeConsole.ModeType.COMMON.name());
        String methodName = MethodConsole.MethodType.REGISTER.name();
        RequestData requestData = new RequestData("테스트 책이름1", "테스트 책저자", 100);
        Server.requestMethod(new Request(methodName, requestData));
        requestData = new RequestData("테스트 책이름2", "테스트 책저자", 100);
        Server.requestMethod(new Request(methodName, requestData));
        requestData = new RequestData(100); // 애플리케이션 중단(예외 발생), book.json에 정보 업데이트
    }

}