package com.codegym.model.book;

import com.codegym.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String description;

    private String image;

    private String status;

    private String publisher;

    private int quantity;

    public static final String OLD = "Cũ";
    public static final String NEW = "Mới";
    public static List<String> conditions = new ArrayList<>();
    static {
        conditions.add(OLD);
        conditions.add(NEW);
    }
}
