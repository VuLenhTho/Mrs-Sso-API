package com.vulenhtho.mrssso.service.impl;

import com.vulenhtho.mrssso.dto.RoleDTO;
import com.vulenhtho.mrssso.mapper.RoleMapper;
import com.vulenhtho.mrssso.repository.RoleRepository;
import com.vulenhtho.mrssso.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public Set<RoleDTO> getAll() {
        return roleMapper.toDTO(new HashSet<>(roleRepository.findAll()));
    }
}
