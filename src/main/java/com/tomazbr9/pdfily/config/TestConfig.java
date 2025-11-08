package com.tomazbr9.pdfily.config;

import com.tomazbr9.pdfily.enums.RoleName;
import com.tomazbr9.pdfily.model.RoleModel;
import com.tomazbr9.pdfily.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public void run(String... args) throws Exception {

        RoleModel adminRole = RoleModel.builder()
                .name(RoleName.ROLE_ADMIN)
                .build();

        RoleModel customerRole = RoleModel.builder()
                .name(RoleName.ROLE_USER)
                .build();

        roleRepository.saveAll(Arrays.asList(adminRole, customerRole));
    }

}