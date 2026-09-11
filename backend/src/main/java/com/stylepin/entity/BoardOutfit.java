package com.stylepin.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity
@Table(name="board_outfits",uniqueConstraints=@UniqueConstraint(columnNames={"board_id","outfit_id"}),
    indexes={@Index(columnList="board_id,createdAt"),@Index(columnList="outfit_id")})
public class BoardOutfit {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="board_id",nullable=false) private Board board;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="outfit_id",nullable=false) private Outfit outfit;
    @Column(nullable=false,updatable=false) private Instant createdAt=Instant.now();
    protected BoardOutfit(){}
    public BoardOutfit(Board board,Outfit outfit){this.board=board;this.outfit=outfit;}
}
