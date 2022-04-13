package com.codegym.model.book;

import com.codegym.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookForm {
    private Long id;

    private String name;

    private Category category;

    private String description;

    private MultipartFile image;

    private String status;

    private String publisher;

    private int quantity;

    public static final String OLD = "Cũ";
    public static final String NEW = "Mới";
    public static List<String> statuses = new ArrayList<>();
    static {
        statuses.add(OLD);
        statuses.add(NEW);
    }
}
