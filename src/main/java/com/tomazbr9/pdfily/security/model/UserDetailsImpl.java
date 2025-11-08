package com.tomazbr9.pdfily.security.model;

import com.tomazbr9.pdfily.model.UserModel;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class UserDetailsImpl implements UserDetails {

    // Entidade User que criamos anteriormente
    private UserModel user;

    // Construtor que recebe o usuário da aplicação
    public UserDetailsImpl(UserModel user) {
        this.user = user;
    }

    /**
     * Retorna as permissões (autoridades) do usuário.
     * O Spring Security usa isso para verificar o que o usuário pode ou não fazer.
     *
     * Aqui mapeamos os papéis do usuário (ex: ROLE_USER, ROLE_ADMIN)
     * para objetos do tipo GrantedAuthority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name())) // Converte Enum para String
                .toList();
    }

    /**
     * Retorna o identificador do usuário (geralmente o username ou email).
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }


    /**
     * Retorna a senha do usuário.
     * Usado na autenticação.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Indica se a conta está expirada.
     * true = conta não expirada (ainda válida)
     * Pode ser alterado para retornar um campo vindo do banco, se desejar.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica se a conta está bloqueada.
     * true = conta desbloqueada
     * Idealmente, deveria ser baseado em um campo no UserModel.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica se as credenciais (senha, etc.) estão expiradas.
     * true = válidas
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica se a conta está habilitada para uso.
     * true = ativa
     * Pode ser usado para desativar contas manualmente.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
