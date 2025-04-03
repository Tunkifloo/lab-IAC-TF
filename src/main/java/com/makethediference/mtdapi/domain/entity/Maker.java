package com.makethediference.mtdapi.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "Maker")
@Table(name = "makers")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Maker extends User {
}
