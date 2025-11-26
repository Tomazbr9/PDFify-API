package com.tomazbr9.pdfily.downloadhistory.service;

import com.tomazbr9.pdfily.conversion.model.ConversionModel;
import com.tomazbr9.pdfily.downloadhistory.model.DownloadHistoryModel;
import com.tomazbr9.pdfily.exception.ResourceDoesNotBelongToTheAuthenticatedUser;
import com.tomazbr9.pdfily.user.model.UserModel;
import org.slf4j.LoggerFactory;

public class DownloadValidationService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DownloadValidationService.class);

    // Verifica download pertence ao usuário que está tentando baixar
    public void validateIfUserCanDownload(ConversionModel conversion, UserModel user){
        String username = conversion.getFileUploadModel().getUser().getUsername();
        if(!username.equals(user.getUsername())){
            logger.warn("Usuário {} tentou baixar um arquivo que pertence a {} ", username, user.getUsername());
            throw new ResourceDoesNotBelongToTheAuthenticatedUser("Usuário sem permissão para fazer download do arquivo");
        }
    }

    // Verifica se usuário que quer deletar registro de download é o dono do recurso
    public void validateIfUserCanDeleteDownloadLog(DownloadHistoryModel downloadLog, UserModel user){
        String username = downloadLog.getUser().getUsername();
        if(!username.equals(user.getUsername())){
            logger.warn("Usuário {} tentou deletar um arquivo que pertence a {} ", username, user.getUsername());
            throw new ResourceDoesNotBelongToTheAuthenticatedUser("Usuário sem permissão para deletar registro de download");
        }
    }

}
