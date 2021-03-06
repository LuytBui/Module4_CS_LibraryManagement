package com.codegym.model.ticket;

import com.codegym.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "borrowTickets")
public class BorrowTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User customer;

    @Column(columnDefinition = "DATETIME")
    private String borrowDate;

    private int duration;

    @Column(columnDefinition = "boolean default false")
    private boolean isAccepted;

    @Column(columnDefinition = "boolean default false")
    private boolean isReviewed;

    @Column(columnDefinition = "boolean default false")
    private boolean isReturned;

    @Column(columnDefinition = "boolean default false")
    private boolean hasReturnTicket;
}
