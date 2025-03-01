package com.duchung.vn.controller;

import com.duchung.vn.dto.UserCreateRequest;
import com.duchung.vn.dto.UserDTO;
import com.duchung.vn.dto.UserUpdateRequest;
import com.duchung.vn.enumeration.RoleType;
import com.duchung.vn.service.UserService;
import com.duchung.vn.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController{

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users with filtering options")
    public ResponseEntity<ResponseUtils.ApiResponse<ResponseUtils.PageResponse<UserDTO>>> getAllUsers(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) RoleType role,
            @RequestParam(required = false) String school,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {

        Page<UserDTO> page = userService.findBySearchCriteria(
                fullName, email, username, role, school, phone, fromDate, toDate, active, pageable);

        return ResponseUtils.createPageResponse(page);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isCurrentUser(#id)")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<ResponseUtils.ApiResponse<UserDTO>> getUserById(
            @Parameter(description = "User ID", required = true) @PathVariable Long id) {

        return userService.findById(id)
                .map(userDTO -> ResponseUtils.success(userDTO, "User retrieved successfully"))
                .orElseThrow(() -> new com.duchung.vn.exception.ResourceNotFoundException("User", "id", id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new user")
    public ResponseEntity<ResponseUtils.ApiResponse<UserDTO>> createUser(
            @Valid @RequestBody UserCreateRequest request) {

        UserDTO createdUser = userService.createUser(request);
        return ResponseUtils.created(createdUser, "User created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user by ID")
    public ResponseEntity<ResponseUtils.ApiResponse<UserDTO>> updateUser(
            @Parameter(description = "User ID", required = true) @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {

        UserDTO updatedUser = userService.updateUser(id, request);
        return ResponseUtils.success(updatedUser, "User updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user (soft delete)")
    public ResponseEntity<ResponseUtils.ApiResponse<Void>> deleteUser(
            @Parameter(description = "User ID", required = true) @PathVariable Long id) {

        userService.softDelete(id);
        return ResponseUtils.success(null, "User deleted successfully");
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isCurrentUser(#id)")
    @Operation(summary = "Change user password")
    public ResponseEntity<ResponseUtils.ApiResponse<Void>> changePassword(
            @Parameter(description = "User ID", required = true) @PathVariable Long id,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {

        userService.changePassword(id, currentPassword, newPassword);
        return ResponseUtils.success(null, "Password changed successfully");
    }

    @GetMapping("/by-role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get users by role")
    public ResponseEntity<ResponseUtils.ApiResponse<java.util.List<UserDTO>>> getUsersByRole(
            @Parameter(description = "Role type", required = true) @PathVariable RoleType role) {

        return ResponseUtils.success(userService.findByRole(role), "Users retrieved successfully");
    }
}