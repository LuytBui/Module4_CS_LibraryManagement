package com.codegym.model.ticket;

import com.codegym.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "borrowTicketDetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowTicketDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private BorrowTicket borrowTicket;

    @ManyToOne
    private Book book;
}