package com.klaha.renthub.categories.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="name",length = 64,nullable = false)
    private String name;
}
