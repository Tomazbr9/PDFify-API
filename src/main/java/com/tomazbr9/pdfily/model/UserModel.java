package com.tomazbr9.pdfily.model;

import jakarta.persistence.*;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_user")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
@Setter
public class UserModel implements Serializable {

    private static final long seriaVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String password;

    private LocalDate created_at;
    private LocalDate updated_at;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<RoleModel> roles;
}
