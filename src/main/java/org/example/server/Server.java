package org.example.server;

import org.example.packet.Request;
import org.example.server.controller.BookController;
import org.example.server.controller.Controller;
import org.example.server.repository.FileRepository;
import org.example.server.repository.InMemoryRepository;
import org.example.server.repository.Repository;
import org.example.server.service.BookService;
import org.example.server.service.Service;

import java.util.function.Supplier;

// 서버는 싱글톤으로 생성
// 기능 1: 클라이언트로부터 받은 모드에 따라 controller, service, repository 세팅
// 기능 2: 클라이언트로부터 받은 메뉴(요청) 수행하고 String으로 응답
public class Server {
    private enum ModeType { // 모드가 늘어날 경우를 대비하기 위해, enum으로 레포지토리 생성 기능 매핑.
        COMMON(FileRepository::new),
        TEST(InMemoryRepository::new);

        private final Supplier<Repository> RepositorySupplier;

        ModeType(Supplier<Repository> RepositorySupplier) {
            this.RepositorySupplier = RepositorySupplier;
        }

        public Repository getRepository() {
            return RepositorySupplier.get();
        }
    }

    private Server() {
    }

    private static Controller controller;
    private static Repository repository;
    private static int saveTimer;

    public static void setServer(String mode) {
        repository = ModeType.valueOf(mode).getRepository();
        Service service = new BookService(repository);
        controller = new BookController(service);
        saveTimer = 0;
    }

    public static String requestMethod(Request request) {
        try {
            if (++saveTimer == 5) {
                saveTimer = 0;
                saveData();
            }
            return controller.mapController(request);
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public static void saveData() {
        if (repository instanceof FileRepository) {
            repository.save();
        }
    }
}
