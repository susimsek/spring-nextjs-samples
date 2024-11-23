package io.github.susimsek.springnextjssamples.service.mapper;

import io.github.susimsek.springnextjssamples.entity.UserEntity;
import io.github.susimsek.springnextjssamples.entity.UserRoleMappingEntity;
import java.util.Set;
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
            .authorities(mapRolesToAuthorities(userEntity.getRoles()))
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(!userEntity.getEnabled())
            .build();
    }


    default Set<SimpleGrantedAuthority> mapRolesToAuthorities(Set<UserRoleMappingEntity> roles) {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getRole().getName()))
            .collect(Collectors.toSet());
    }

}
