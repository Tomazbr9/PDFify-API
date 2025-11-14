package com.tomazbr9.pdfily.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_file_upload")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
@Setter
public class FileUploadModel implements Serializable {

    private static final long seriaVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;
    private String originalName;
    private String filePath;
    private Long fileSize;

    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name = "tb_user", nullable = false)
    private UserModel user;
}
