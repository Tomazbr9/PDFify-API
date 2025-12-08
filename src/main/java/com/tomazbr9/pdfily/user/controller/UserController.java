package com.tomazbr9.pdfily.user.controller;

import com.tomazbr9.pdfily.dto.userDTO.UserPutDTO;
import com.tomazbr9.pdfily.dto.userDTO.UserRequestDTO;
import com.tomazbr9.pdfily.dto.userDTO.UserResponseDTO;
import com.tomazbr9.pdfily.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Usuários",
        description = "Operações para recuperar, atualizar e deletar usuários"
)
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService service;

    @Operation(
            summary = "Buscar usuário autenticado",
            description = "Retorna os dados do usuário atualmente autenticado no sistema."
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = UserResponseDTO.class))),
                @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                        content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @GetMapping
    public ResponseEntity<UserResponseDTO> userData(@AuthenticationPrincipal UserDetails userDetails){
        UserResponseDTO response = service.userData(userDetails);
        return ResponseEntity.ok().body(response);

    }

    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza as informações do usuário autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping
    public ResponseEntity<UserResponseDTO> userPut(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para atualização de usuário",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserPutDTO.class))
            )
            @RequestBody UserPutDTO request, @AuthenticationPrincipal UserDetails userDetails){
        UserResponseDTO response = service.userPut(request, userDetails);
        return ResponseEntity.ok().body(response);
    }

    @Operation(
            summary = "Excluir usuário",
            description = "Remove a conta do usuário autenticado do sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping
    public ResponseEntity<Void> userDelete(@AuthenticationPrincipal UserDetails userDetails){
        service.userDelete(userDetails);
        return ResponseEntity.noContent().build();

    }

}
