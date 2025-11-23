package com.tomazbr9.pdfily.downloadhistory.repository;

import com.tomazbr9.pdfily.downloadhistory.model.DownloadHistoryModel;
import com.tomazbr9.pdfily.user.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DownloadHistoryRepository extends JpaRepository<DownloadHistoryModel, UUID> {

    List<DownloadHistoryModel> findDownloadHistoryByUser(UserModel user);

}
