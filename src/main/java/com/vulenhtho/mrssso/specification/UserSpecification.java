package com.vulenhtho.mrssso.specification;

import com.vulenhtho.mrssso.dto.request.UserFilterRequestDTO;
import com.vulenhtho.mrssso.entity.Role;
import com.vulenhtho.mrssso.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserSpecification {
    public static Specification<User> filterUser(UserFilterRequestDTO filter) {
            return Specification
                    .where(withSex(filter.getSex()))
                    .and(withActivated(filter.getActivated()))
                    .and(withLocked(filter.getLocked()))
                    .and(withRoles(filter.getRole()))
                    .and(Specification.where(withCreatedBy(filter.getSearch()))
                            .or(withEmail(filter.getSearch()))
                            .or(withFullName(filter.getSearch()))
                            .or(withPhone(filter.getSearch()))
                            .or(withUserName(filter.getSearch()))
                    );
    }


    public static Specification<User> withActivated(Boolean activated) {
        if (activated == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("activated"), activated);
    }

    public static Specification<User> withLocked(Boolean locked) {
        if (locked == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("locked"), locked);
    }

    public static Specification<User> withSex(Boolean sex) {
        if (sex == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("sex"), sex);
    }

    public static Specification<User> withCreatedBy(String search) {
        if (search == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("createdBy"), "%" + search + "%");
    }

    public static Specification<User> withUserName(String search) {
        if (search == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("userName"), "%" + search + "%");
    }

    public static Specification<User> withFullName(String search) {
        if (search == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("fullName"), "%" + search + "%");
    }

    public static Specification<User> withEmail(String search) {
        if (search == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("email"), "%" + search + "%");
    }

    public static Specification<User> withPhone(String search) {
        if (search == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("phone"), "%" + search + "%");
    }

    public static Specification<User> withRoles(String roleIds) {
        if (roleIds == null)
            return null;
        List<Long> idLongs = Arrays.stream(roleIds.split(",")).map(Long::parseLong).collect(Collectors.toList());

        return (root, criteriaQuery, criteriaBuilder)
                -> {
            criteriaQuery.distinct(true);
            Root<Role> roleRoot = criteriaQuery.from(Role.class);
            Expression<Collection<User>> userOfRole = roleRoot.get("users");
            return criteriaBuilder.and(roleRoot.get("id").in(idLongs), criteriaBuilder.isMember(root, userOfRole));
        };
    }


}
