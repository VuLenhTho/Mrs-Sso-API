package com.vulenhtho.mrssso.specification;

import com.vulenhtho.mrssso.dto.request.ProductFilterRequestDTO;
import com.vulenhtho.mrssso.entity.Discount;
import com.vulenhtho.mrssso.entity.Product;
import com.vulenhtho.mrssso.entity.enumeration.ProductStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ProductSpecification {

    public static Specification<Product> filterProduct(ProductFilterRequestDTO filter) {
            return Specification
                    .where(withStatus(filter.getStatus()))
                    .and(withTrend(filter.getTrend()))
                    .and(withSubCategory(filter.getSubCategoryId()))
                    .and(withHot(filter.getHot()))
                    .and(withDiscount(filter.getDiscountId()))
                    .and(withName(filter.getSearch()));

    }

    public static Specification<Product> withStatus(ProductStatus status) {
        if (status == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Product> s() {
        return (root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.isNotNull(root.get("id"));
    }

    public static Specification<Product> withSubCategory(Long id) {
        if (id == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("subCategory").get("id"), id);
    }

    public static Specification<Product> withDiscount(String  ids) {
        if (ids == null)
            return null;
        List<Long> idLongs = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());

        return (root, criteriaQuery, criteriaBuilder)
                -> {
            criteriaQuery.distinct(true);
            Root<Discount> discountRoot = criteriaQuery.from(Discount.class);
            Expression<Collection<Product>> productOfDiscount = discountRoot.get("products");
            return criteriaBuilder.and(discountRoot.get("id").in(idLongs), criteriaBuilder.isMember(root, productOfDiscount));
        };
    }

    public static Specification<Product> withName(String name) {
        if (name == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Product> withTrend(Boolean trend) {
        if (trend == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("trend"), trend);
    }

    public static Specification<Product> withHot(Boolean hot) {
        if (hot == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("hot"), hot);
    }

    public static Specification<Product> withCreatedBy(String createdBy) {
        if (createdBy == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.like(root.get("createdBy"), "%" + createdBy + "%");
    }

    public static Specification<Product> withPrice(Long maxPrice, Long minPrice) {
        if (maxPrice == null || minPrice == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice),
                        criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
    }
}
