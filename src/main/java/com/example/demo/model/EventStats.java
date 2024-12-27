package com.example.demo.model;

import jakarta.persistence.*;


@Entity
@Table(name = "event_stats")
public class EventStats {
    @Id
    private Long eventId;

    private int registrationsCount;
    private double averageRating;

    @OneToOne
    @MapsId // This means it shares the same ID with Event
    @JoinColumn(name = "eventId")
    private Event event;

    // Getters and Setters
}

