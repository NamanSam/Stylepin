package com.stylepin.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity
@Table(name="saved_outfits",uniqueConstraints=@UniqueConstraint(columnNames={"user_id","outfit_id"}),
    indexes={@Index(columnList="user_id,createdAt"),@Index(columnList="outfit_id")})
public class SavedOutfit {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="user_id",nullable=false) private User user;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="outfit_id",nullable=false) private Outfit outfit;
    @Column(nullable=false,updatable=false) private Instant createdAt=Instant.now();
    protected SavedOutfit(){}
    public SavedOutfit(User user,Outfit outfit){this.user=user;this.outfit=outfit;}
}
