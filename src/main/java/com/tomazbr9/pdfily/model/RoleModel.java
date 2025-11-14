package com.tomazbr9.pdfily.model;

import com.tomazbr9.pdfily.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tb_role")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
@Setter
public class RoleModel implements Serializable {
    private static final long seriaVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private RoleName name;

}
