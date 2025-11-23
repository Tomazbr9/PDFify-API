package com.tomazbr9.pdfily.conversion.repository;

import com.tomazbr9.pdfily.conversion.model.ConversionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConversionRepository extends JpaRepository<ConversionModel, UUID> {

}
