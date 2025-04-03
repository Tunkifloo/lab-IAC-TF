package com.makethediference.mtdapi.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "LandingFiles")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "landingFiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LandingFiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLandingFiles;
    private String fileTypes;
    private String fileName;
    @Enumerated(EnumType.STRING)
    private FileSector fileSector;
    private String makerName;
    private String teamName;
    private String description;
    private String stand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Admin admin;
}
