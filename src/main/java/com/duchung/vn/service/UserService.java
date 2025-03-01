package com.duchung.vn.service;

import com.duchung.vn.dto.UserCreateRequest;
import com.duchung.vn.dto.UserDTO;
import com.duchung.vn.dto.UserUpdateRequest;
import com.duchung.vn.entity.User;
import com.duchung.vn.enumeration.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService extends BaseService<User, UserDTO> {

    UserDTO createUser(UserCreateRequest request);

    UserDTO updateUser(Long id, UserUpdateRequest request);

    Optional<UserDTO> findByUsername(String username);

    Optional<UserDTO> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<UserDTO> findByRole(RoleType role);

    Page<UserDTO> findBySearchCriteria(
            String fullName,
            String email,
            String username,
            RoleType role,
            String school,
            String phone,
            LocalDate fromDate,
            LocalDate toDate,
            Boolean active,
            Pageable pageable);

    void changePassword(Long id, String currentPassword, String newPassword);
}