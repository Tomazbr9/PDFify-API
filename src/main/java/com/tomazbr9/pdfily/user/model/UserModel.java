package com.tomazbr9.pdfily.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tomazbr9.pdfily.downloadhistory.model.DownloadHistoryModel;
import com.tomazbr9.pdfily.fileupload.model.FileUploadModel;
import com.tomazbr9.pdfily.role.model.RoleModel;
import jakarta.persistence.*;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private UUID id;
    private String username;
    private String password;

    private LocalDate created_at;
    private LocalDate updated_at;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DownloadHistoryModel> downloadsHistoryModel = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileUploadModel> fileUploadsModel = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private List<RoleModel> roles;
}
