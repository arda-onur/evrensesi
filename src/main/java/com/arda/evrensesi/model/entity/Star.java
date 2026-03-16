package com.arda.evrensesi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "stars", uniqueConstraints = {
        @UniqueConstraint(name = "uk_star_xy", columnNames = {"x","y"})
         })
@RequiredArgsConstructor
@Getter
@Setter
public class Star {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(name = "es_indexed", nullable = false)
    private boolean esIndexed = false;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
    }
}
