package com.stylepin.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity
@Table(name="boards", indexes=@Index(columnList="owner_id"))
public class Board {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="owner_id",nullable=false) private User owner;
    @Column(nullable=false,length=100) private String name;
    @Column(length=1000) private String description;
    @Column(nullable=false,updatable=false) private Instant createdAt=Instant.now();
    @Column(nullable=false) private Instant updatedAt=Instant.now();
    protected Board() {}
    public Board(User owner,String name,String description) { this.owner=owner;this.name=name;this.description=description; }
    public Long getId(){return id;}
    public User getOwner(){return owner;}
    public String getName(){return name;}
    public String getDescription(){return description;}
    public Instant getCreatedAt(){return createdAt;}
    @PreUpdate void updated(){updatedAt=Instant.now();}
}
