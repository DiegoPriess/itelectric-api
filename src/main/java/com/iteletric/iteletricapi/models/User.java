package com.iteletric.iteletricapi.models;

import com.iteletric.iteletricapi.config.baseentities.BaseModel;
import com.iteletric.iteletricapi.enums.user.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseModel {

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="deleted", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer deleted;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName role;
}
