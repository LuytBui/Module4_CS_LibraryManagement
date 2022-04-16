package com.codegym.model.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "returnTickets")
public class ReturnTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    BorrowTicket borrowTicket;

    @Column(columnDefinition = "DATETIME")
    private String returnDate;

    private String status;
    private boolean accept;

    public ReturnTicket(BorrowTicket borrowTicket) {
        this.borrowTicket = borrowTicket;
    }

    public static final String OVERDUE = "Quá hạn";
    public static final String RETURNED = "Đã trả";
    public static List<String> statuses = new ArrayList<>();
    static {
        statuses.add(OVERDUE);
        statuses.add(RETURNED);
    }
}
