package com.myvamsnet.monpa.specification;

import com.myvamsnet.monpa.dto.admin.AdminUserFilter;
import com.myvamsnet.monpa.model.AccountStatus;
import com.myvamsnet.monpa.model.Role;
import com.myvamsnet.monpa.model.User;
import org.springframework.data.jpa.domain.Specification;

public final class AdminUserSpecification {

    private AdminUserSpecification() {
    }

    public static Specification<User> build(
            AdminUserFilter filter
    ) {

        return Specification

                .where(search(filter.getSearch()))

                .and(hasRole(filter.getRole()))

                .and(hasAccountStatus(
                        filter.getAccountStatus()
                ));

    }

    public static Specification<User> search(String search) {

        return (root, query, cb) -> {

            if (search == null || search.isBlank()) {
                return cb.conjunction();
            }

            String keyword = "%" + search.toLowerCase() + "%";

            return cb.or(

                    cb.like(
                            cb.lower(root.get("fullName")),
                            keyword
                    ),

                    cb.like(
                            cb.lower(root.get("email")),
                            keyword
                    ),

                    cb.like(
                            cb.lower(root.get("phoneNumber")),
                            keyword
                    )

            );

        };

    }

    public static Specification<User> hasRole(Role role) {

        return (root, query, cb) -> {

            if (role == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("role"),
                    role
            );

        };

    }

    public static Specification<User> hasAccountStatus(
            AccountStatus status
    ) {

        return (root, query, cb) -> {

            if (status == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("accountStatus"),
                    status
            );

        };

    }

}
