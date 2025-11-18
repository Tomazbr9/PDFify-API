package com.tomazbr9.pdfily.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    private UUID conversionId;

    private UserModel userID;

    private LocalDateTime downloadedAt;

}
