package com.example.demo.model;

import jakarta.persistence.*;



@Entity
@Table(name = "event_ratings")
public class EventRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private int rating; // Rating between 1 and 5

    // Getters and Setters
}
