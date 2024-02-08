package com.mshembelev.mindskeeper.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task")
public class TaskModel {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_seq")
    @SequenceGenerator(name = "task_id_seq", sequenceName = "task_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "groupId", nullable = false)
    private Long groupId;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "currentStatus", nullable = false)
    private Boolean currentStatus;
}
