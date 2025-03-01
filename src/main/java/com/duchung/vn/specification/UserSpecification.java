package com.duchung.vn.specification;

import com.duchung.vn.entity.User;
import com.duchung.vn.enumeration.RoleType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    private UserSpecification() {
        // Private constructor to prevent instantiation
    }

    public static Specification<User> withFullNameLike(String fullName) {
        return (root, query, criteriaBuilder) -> {
            if (fullName == null || fullName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("fullName")),
                    "%" + fullName.toLowerCase() + "%");
        };
    }

    public static Specification<User> withEmailLike(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")),
                    "%" + email.toLowerCase() + "%");
        };
    }

    public static Specification<User> withUsernameLike(String username) {
        return (root, query, criteriaBuilder) -> {
            if (username == null || username.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("username")),
                    "%" + username.toLowerCase() + "%");
        };
    }

    public static Specification<User> withRole(RoleType role) {
        return (root, query, criteriaBuilder) -> {
            if (role == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("role"), role);
        };
    }

    public static Specification<User> withSchoolLike(String school) {
        return (root, query, criteriaBuilder) -> {
            if (school == null || school.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("school")),
                    "%" + school.toLowerCase() + "%");
        };
    }

    public static Specification<User> withPhone(String phone) {
        return (root, query, criteriaBuilder) -> {
            if (phone == null || phone.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("phoneNumber"), "%" + phone + "%");
        };
    }

    public static Specification<User> withDateOfBirthBetween(LocalDate from, LocalDate to) {
        return (root, query, criteriaBuilder) -> {
            if (from == null && to == null) {
                return criteriaBuilder.conjunction();
            }

            if (from == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("dateOfBirth"), to);
            }

            if (to == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("dateOfBirth"), from);
            }

            return criteriaBuilder.between(root.get("dateOfBirth"), from, to);
        };
    }

    public static Specification<User> buildSpecification(
            String fullName,
            String email,
            String username,
            RoleType role,
            String school,
            String phone,
            LocalDate fromDate,
            LocalDate toDate,
            Boolean active) {

        return Specification
                .where(withFullNameLike(fullName))
                .and(withEmailLike(email))
                .and(withUsernameLike(username))
                .and(withRole(role))
                .and(withSchoolLike(school))
                .and(withPhone(phone))
                .and(withDateOfBirthBetween(fromDate, toDate))
                .and((root, query, criteriaBuilder) -> {
                    if (active == null) {
                        return criteriaBuilder.conjunction();
                    }
                    return criteriaBuilder.equal(root.get("active"), active);
                });
    }
}