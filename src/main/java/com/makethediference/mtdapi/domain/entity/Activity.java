package com.makethediference.mtdapi.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "Activity")
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityId;
    private String name;
    private String description;
    private String format;
    private String type;
    private String pillar;
    private LocalDate startDate;
    private LocalDateTime duration;
    @Enumerated(EnumType.STRING)
    private ActStatus status;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<File> files;

    @ManyToOne
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;
}
