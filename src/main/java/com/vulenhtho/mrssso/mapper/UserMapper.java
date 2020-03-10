package com.vulenhtho.mrssso.mapper;

import com.vulenhtho.mrssso.dto.UserDTO;
import com.vulenhtho.mrssso.entity.User;
import com.vulenhtho.mrssso.repository.RoleRepository;
import com.vulenhtho.mrssso.util.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private RoleRepository roleRepository;
    private RoleMapper roleMapper;

    public UserMapper(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }


    public UserDTO toDTO(User user){
        UserDTO userDTO = new UserDTO();
        BeanUtils.refine(user, userDTO, BeanUtils::copyNonNull);
        if (user.getRoles() != null && !user.getRoles().isEmpty()){
            userDTO.setRoles(roleMapper.toDTO(user.getRoles()));
        }else userDTO.setRoles(null);

        userDTO.setPassword(null);
        return userDTO;
    }

    public List<UserDTO> toDTO(List<User> users){
        return users.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public User toEntity(UserDTO userDTO){
        User user = new User();
        BeanUtils.refine(userDTO, user, BeanUtils::copyNonNull);

        if (!CollectionUtils.isEmpty(userDTO.getRoles())){
            user.setRoles(roleMapper.toEntity(userDTO.getRoles()));
        }else user.setRoles(null);

        return user;
    }

    public List<User> toEntity(List<UserDTO> userDTOs){
        return userDTOs.stream().map(this::toEntity).collect(Collectors.toList());
    }

}
