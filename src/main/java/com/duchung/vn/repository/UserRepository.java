package com.duchung.vn.repository;

import com.duchung.vn.entity.User;
import com.duchung.vn.enumeration.RoleType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameAndActive(String username, Boolean active);

    Optional<User> findByEmailAndActive(String email, Boolean active);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByRole(RoleType role);

    List<User> findByRoleAndActive(RoleType role, Boolean active);
}