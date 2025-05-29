package rw.ne.java.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_code", nullable = false)
    private Employee employee;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    private MessageStatus status = MessageStatus.SENT;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum MessageStatus {
        SENT, DELIVERED, READ
    }
}