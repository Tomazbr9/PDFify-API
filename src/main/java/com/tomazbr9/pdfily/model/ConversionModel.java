package com.tomazbr9.pdfily.model;

import com.tomazbr9.pdfily.enums.StatusName;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_conversion")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
@Setter
public class ConversionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "tb_file_upload", nullable = false)
    private FileUploadModel fileUploadModel;

    @Enumerated(EnumType.STRING)
    private String targetFormat;

    @Enumerated(EnumType.STRING)
    private StatusName status;

    private String outputPath;
    private LocalDateTime createdAt;

}
