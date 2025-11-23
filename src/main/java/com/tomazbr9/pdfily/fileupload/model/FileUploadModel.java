package com.tomazbr9.pdfily.fileupload.model;

import com.tomazbr9.pdfily.user.model.UserModel;
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
    private Double fileSize;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;
}
