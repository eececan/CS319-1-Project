package com.project.btoproject.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "all_users")
@DiscriminatorValue("HEAD_SECRETARY")
public class HeadSecretary extends User{

}
