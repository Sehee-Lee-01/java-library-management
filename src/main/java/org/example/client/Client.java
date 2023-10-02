package org.example.client;

import org.example.client.console.MethodConsole;
import org.example.client.console.ModeConsole;
import org.example.client.console.ValidateException;
import org.example.client.io.IO;
import org.example.packet.Request;

// 클라이언트는 싱글톤으로 생성 **싱글톤패턴, 레이지로딩
// ** (+ 추후 결합도, 의존성)
public class Client {
    private static final IO io = new IO();

    private Client() {

    }

    public static String scanMode() {
        try {
            return ModeConsole.scanType(io);
        } catch (ValidateException e) {
            io.println(e.getMessage());
            return ModeConsole.scanType(io);
        }
    }

    public static Request scanMenu() {
        try {
            return MethodConsole.scanTypeAndInfo(io);
        } catch (ValidateException e) {
            io.println(e.getMessage());
            return MethodConsole.scanTypeAndInfo(io); // 한 번 더 시도
        }
    }

    public static void printResponse(String response) {
        io.println(response);
    }
}
