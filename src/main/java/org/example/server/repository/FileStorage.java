package org.example.server.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.server.entity.Book;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

// * java 구글 스타일 가이드
public class FileStorage {
    public int newId; //  생성 예정인 id 값, 1부터 생성, JSON에 저장되어 있다. ** id 값을 관리해야할까? 별도 클래스두거나 레포지토리에서 하기
    public final LinkedHashMap<Integer, Book> data;
    private final File jsonFile;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FileStorage() {// + jsonFile을 생성자에서 받을 수 있 게 한다면? uitll 클래스: 파일 스토리지 여러개를 만드는 상황이라면?
        try {
            URI path = Paths.get("src", "main", "resources", "book.json").toUri();
            jsonFile = new File(path);
            Map<String, Object> jsonMap = objectMapper.readValue(jsonFile, Map.class);
            newId = (int) jsonMap.get("new_id");
            data = new LinkedHashMap<>();
            if (newId != 1) { // *고민해보기
                ArrayList<LinkedHashMap<String, Object>> dataObjects = (ArrayList<LinkedHashMap<String, Object>>) jsonMap.get("data");
                putObjectsToBookData(dataObjects);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void putObjectsToBookData(ArrayList<LinkedHashMap<String, Object>> objects) {
        objects.forEach(
                (bookInfoMap) -> {
                    int id = (int) bookInfoMap.get("id");
                    String name = (String) bookInfoMap.get("name");
                    String author = (String) bookInfoMap.get("author");
                    int pages = (int) bookInfoMap.get("pages");
                    String state = (String) bookInfoMap.get("state");
                    String endLoadTime = (String) bookInfoMap.get("endLoadTime");
                    data.put(id, new Book(id, name, author, pages, state, endLoadTime));
                }
        );
    }

    public void saveFile() { // + 파라미터에 path
        Map<String, Object> jsonMap = new HashMap<>();
        if (data.isEmpty()) {
            jsonMap.put("new_id", 1);
            jsonMap.put("data", new ArrayList<>());
        } else {
            jsonMap.put("new_id", newId);
            ArrayList<LinkedHashMap<String, Object>> dataObjects = new ArrayList<>();
            // Book to LinkedHashMap<String, Object>
            putBookDataToObjects(dataObjects);
            jsonMap.put("data", dataObjects);
        }
        /* JSON 파일 저장 */
        try (FileWriter fileWriter = new FileWriter(jsonFile)) {
            String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap);
            fileWriter.write(jsonStr);
            fileWriter.flush();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private void putBookDataToObjects(ArrayList<LinkedHashMap<String, Object>> objects) {
        data.values().stream().forEach(
                book -> {
                    LinkedHashMap<String, Object> bookInfoMap = new LinkedHashMap<>();
                    bookInfoMap.put("id", book.id);
                    bookInfoMap.put("name", book.name);
                    bookInfoMap.put("author", book.author);
                    bookInfoMap.put("pages", book.pages);
                    bookInfoMap.put("state", book.state);
                    bookInfoMap.put("endLoadTime", book.endLoadTime);
                    objects.add(bookInfoMap);
                }
        );
    }
}
