package com.tomazbr9.pdfily.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_download_history")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
@Setter
public class DownloadHistoryModel {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "conversion_id")
    private ConversionModel conversion;

    private LocalDateTime downloadedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

}
