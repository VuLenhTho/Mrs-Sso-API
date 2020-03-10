package com.vulenhtho.mrssso.service.impl;

import com.vulenhtho.mrssso.config.Constant;
import com.vulenhtho.mrssso.dto.UserDTO;
import com.vulenhtho.mrssso.dto.request.UserFilterRequestDTO;
import com.vulenhtho.mrssso.dto.response.UserInfoWebResponseDTO;
import com.vulenhtho.mrssso.entity.Receipt;
import com.vulenhtho.mrssso.entity.Role;
import com.vulenhtho.mrssso.entity.User;
import com.vulenhtho.mrssso.mapper.UserMapper;
import com.vulenhtho.mrssso.repository.RoleRepository;
import com.vulenhtho.mrssso.repository.UserRepository;
import com.vulenhtho.mrssso.service.UserService;
import com.vulenhtho.mrssso.specification.UserSpecification;
import com.vulenhtho.mrssso.util.BeanUtils;
import com.vulenhtho.mrssso.util.RandomUtil;
import com.vulenhtho.mrssso.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public User registerUser(UserDTO userDTO) {
        User newUser = new User();
        BeanUtils.refine(userDTO, newUser, BeanUtils::copyNonNull);
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        newUser.setActivated(false);
        newUser.setLocked(false);
        newUser.setActivationKey(RandomUtil.generateActivationKey());

        Set<Role> roles = new HashSet<>();
        userDTO.getRoles().forEach(roleName -> {
            roleRepository.findByName(roleName.getName()).ifPresent(roles::add);
        });
        newUser.setRoles(roles);
        return userRepository.save(newUser);
    }

    @Override
    public User createdUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setActivated(userDTO.getActivated());
        user.setLocked(userDTO.getLocked());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public boolean activateRegistration(String key) {
        User user = userRepository.findOneByActivationKey(key);
        if (user != null) {
            user.setActivated(true);
            user.setActivationKey(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean changePassword(String currentPassword, String newPassword) {
        User user = userRepository.findByUserName(SecurityUtils.getCurrentUserLogin().get()).get();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        return true;
    }

    @Override
    public User requestPasswordReset(String mail) {
        try {
            User userExits = userRepository.findByEmail(mail).filter(User::getActivated).get();
            userExits.setResetKey(RandomUtil.generateResetKey());
            userExits.setResetDate(Instant.now());
            return userRepository.save(userExits);
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public User completePasswordReset(String newPassword, String key) {
        try {
            User user = userRepository.findByResetKey(key).filter(user1 -> user1.getResetDate().isAfter(Instant.now().minusSeconds(86400))).get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetKey(null);
            user.setResetDate(null);
            return userRepository.save(user);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userMapper.toDTO(userRepository.getOne(id));
    }

    @Override
    public Page<UserDTO> getAllUserWithFilter(UserFilterRequestDTO filterRequest) {
        return userRepository.findAll(UserSpecification.filterUser(filterRequest)
                , PageRequest.of(
                        filterRequest.getPage()
                        , filterRequest.getSize()
                        , sort(filterRequest.getSort())
                )).map(userMapper::toDTO);
    }

    private Sort sort(String typeDateSort) {
        if (typeDateSort != null) {
            switch (typeDateSort) {
                case Constant.DATE_DES:
                    return Sort.by("createdDate").descending();
                case Constant.DATE_ASC:
                    return Sort.by("createdDate").ascending();
                case Constant.MODIFIED_DES:
                    return Sort.by("lastModifiedDate").descending();
            }
        }
        return Sort.by("createdDate").descending();
    }

    @Override
    public boolean update(UserDTO userDTO) {
        Optional<User> userExits = userRepository.findByUserName(userDTO.getUserName());
        if (!userExits.isPresent()) {
            return false;
        }
        User newUser = userMapper.toEntity(userDTO);
        BeanUtils.refine(newUser, userExits.get(), BeanUtils::copyNonNull);
        if (newUser.getPassword() != null) {
            userExits.get().setPassword(passwordEncoder.encode(newUser.getPassword()));
        }
        userRepository.save(userExits.get());
        return true;
    }

    @Override
    public boolean delete(Long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(List<Long> ids) {
        return ids.stream().allMatch(this::delete);
    }

    @Override
    public UserInfoWebResponseDTO getUserLoginInfo() {
        User user = userRepository.findOneWithAuthoritiesByUserName(SecurityUtils.getCurrentUserLogin().get());
        if (user == null) {
            return null;
        }
        UserInfoWebResponseDTO userInfoWebResponseDTO = new UserInfoWebResponseDTO();
        BeanUtils.refine(user, userInfoWebResponseDTO, BeanUtils::copyNonNull);
        List<Long> receiptIds = user.getReceipts().stream().map(Receipt::getId).collect(Collectors.toList());
        userInfoWebResponseDTO.setReceiptIds(receiptIds);

        return userInfoWebResponseDTO;
    }

    @Override
    public String checkDuplicatesUserInfoInCreate(String userName, String email, String phone) {
        Optional<User> userExits = userRepository.findByUserName(userName);
        if (userExits.isPresent()) {
            return Constant.USER_ERROR_MESSAGE.USERNAME_EXISTED;
        }
        userExits = userRepository.findByEmail(email);
        if (userExits.isPresent()) {
            return Constant.USER_ERROR_MESSAGE.EMAIL_EXISTED;
        }
        userExits = userRepository.findByPhone(phone);
        if (userExits.isPresent()) {
            return Constant.USER_ERROR_MESSAGE.PHONE_EXISTED;
        }

        return null;
    }

    @Override
    public String checkDuplicatesUserInfoInUpdate(String userName, String email, String phone) {
        Optional<User> userExits = userRepository.findByEmail(email);
        if (userExits.isPresent() && !Objects.equals(userExits.get().getUserName(), userName)) {
            return Constant.USER_ERROR_MESSAGE.EMAIL_EXISTED;
        }
        userExits = userRepository.findByPhone(phone);
        if (userExits.isPresent() && !Objects.equals(userExits.get().getPhone(), phone)) {
            return Constant.USER_ERROR_MESSAGE.PHONE_EXISTED;
        }

        return null;
    }


}
