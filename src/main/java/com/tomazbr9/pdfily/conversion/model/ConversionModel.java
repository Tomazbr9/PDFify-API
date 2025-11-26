package com.tomazbr9.pdfily.conversion.model;

import com.tomazbr9.pdfily.enums.StatusName;
import com.tomazbr9.pdfily.enums.TargetFormat;
import com.tomazbr9.pdfily.fileupload.model.FileUploadModel;
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
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "tb_file_upload", nullable = false)
    private FileUploadModel fileUploadModel;

    private String convertedFileName;

    @Enumerated(EnumType.STRING)
    private TargetFormat targetFormat;

    @Enumerated(EnumType.STRING)
    private StatusName status;

    private Double size;

    private String outputPath;
    private LocalDateTime createdAt;

}
