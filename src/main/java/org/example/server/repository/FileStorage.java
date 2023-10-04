package org.example.server.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.server.entity.Book;
import org.example.server.entity.bookStatus.BookStatusType;
import org.example.server.entity.bookStatus.LoadStatus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileStorage {
    public final LinkedHashMap<Integer, Book> data;
    private final File jsonFile;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public int newId;

    public FileStorage() {
        try {
            URI path = Paths.get("src", "main", "resources", "book.json").toUri();
            jsonFile = new File(path);
            Map<String, Object> jsonMap = objectMapper.readValue(jsonFile, Map.class);
            newId = (int) jsonMap.get("new_id");
            data = new LinkedHashMap<>();
            if (newId != 1) {
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
                    String status = (String) bookInfoMap.get("status");
                    String endLoadTime = (String) bookInfoMap.get("endLoadTime");
                    data.put(id, new Book(id, name, author, pages, status, endLoadTime));
                }
        );
    }

    public void saveFile() {
        Map<String, Object> jsonMap = new HashMap<>();
        if (data.isEmpty()) {
            jsonMap.put("new_id", 1);
            jsonMap.put("data", new ArrayList<>());
        } else {
            jsonMap.put("new_id", newId);
            ArrayList<LinkedHashMap<String, Object>> dataObjects = new ArrayList<>();
            putBookDataToObjects(dataObjects);
            jsonMap.put("data", dataObjects);
        }
        try (FileWriter fileWriter = new FileWriter(jsonFile)) {
            String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonMap);
            fileWriter.write(jsonStr);
            fileWriter.flush();
        } catch (Exception e) {
            throw new RuntimeException("[System] 파일 쓰기 에러 발생");
        }
    }

    private void putBookDataToObjects(ArrayList<LinkedHashMap<String, Object>> objects) {
        data.values().forEach(
                book -> {
                    LinkedHashMap<String, Object> bookInfoMap = new LinkedHashMap<>();
                    bookInfoMap.put("id", book.id);
                    bookInfoMap.put("name", book.name);
                    bookInfoMap.put("author", book.author);
                    bookInfoMap.put("pages", book.pages);
                    bookInfoMap.put("status", book.status.getBookStatusType().name());
                    if (book.status.getBookStatusType().equals(BookStatusType.LOAD)) {
                        LoadStatus bookStatus = (LoadStatus) book.status;
                        bookInfoMap.put("endLoadTime", bookStatus.getEndLoadTime().toString());
                    } else {
                        bookInfoMap.put("endLoadTime", "");
                    }
                    objects.add(bookInfoMap);
                }
        );
    }
}
