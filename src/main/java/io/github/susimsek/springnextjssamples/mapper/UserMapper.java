package io.github.susimsek.springnextjssamples.mapper;

import io.github.susimsek.springnextjssamples.entity.UserEntity;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper(componentModel = "spring")
public interface UserMapper {

    default UserDetails toDto(UserEntity userEntity) {
        return User.builder()
            .username(userEntity.getUsername())
            .password(userEntity.getPassword())
            .authorities(userEntity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().getName()))
                .collect(Collectors.toSet()))
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(!userEntity.getEnabled())
            .build();
    }
}
