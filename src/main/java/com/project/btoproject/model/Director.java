package com.project.btoproject.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "all_users")
@DiscriminatorValue("DIRECTOR")
public class Director extends User{

}
