package com.makethediference.mtdapi.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "Director")
@Table(name = "directors")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Director extends User{
}
