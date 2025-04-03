package com.makethediference.mtdapi.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "Coordinator")
@Table(name = "coordinators")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Coordinator extends User {
}
