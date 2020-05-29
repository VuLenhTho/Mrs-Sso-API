package com.vulenhtho.mrssso.specification;

import com.vulenhtho.mrssso.dto.request.BillFilterRequest;
import com.vulenhtho.mrssso.entity.Bill;
import com.vulenhtho.mrssso.entity.enumeration.BillStatus;
import com.vulenhtho.mrssso.entity.enumeration.PaymentMethod;
import org.springframework.data.jpa.domain.Specification;

public class BillSpecification {
    public static Specification<Bill> filterBill(BillFilterRequest filter) {
        return Specification
                .where(withStatus(filter.getStatus()))
                .and(withPaymentMethod(filter.getPaymentMethod()))
                .and(Specification.where(withReceiverName(filter.getSearch()))
                        .or(withPhone(filter.getSearch()))
                        .or(withAddress(filter.getSearch()))
                );
    }


    public static Specification<Bill> withPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("paymentMethod"), paymentMethod);
    }

    public static Specification<Bill> withReceiverName(String search) {
        if (search == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("receiver"), "%" + search + "%");
    }

    public static Specification<Bill> withStatus(BillStatus billStatus) {
        if (billStatus == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), billStatus);
    }

    public static Specification<Bill> withPhone(String search) {
        if (search == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("phone"), "%" + search + "%");
    }

    public static Specification<Bill> withAddress(String search) {
        if (search == null)
            return null;

        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("address"), "%" + search + "%");
    }

}
