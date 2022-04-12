package com.codegym.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_LIBRARIAN = "ROLE_LIBRARIAN";
    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
}
