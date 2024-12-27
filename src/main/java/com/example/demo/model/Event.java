package com.example.demo.model;

import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    private String location;

    @ManyToOne
    @JoinColumn(name = "category_id") // If using categories
    private EventCategory category;

    // Getters and Setters
}

