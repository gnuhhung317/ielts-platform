package com.duchung.vn.service.impl;

import com.duchung.vn.dto.UserCreateRequest;
import com.duchung.vn.dto.UserDTO;
import com.duchung.vn.dto.UserUpdateRequest;
import com.duchung.vn.entity.User;
import com.duchung.vn.enumeration.RoleType;
import com.duchung.vn.exception.BadRequestException;
import com.duchung.vn.exception.ResourceNotFoundException;
import com.duchung.vn.mapper.UserMapper;
import com.duchung.vn.repository.UserRepository;
import com.duchung.vn.service.UserService;
import com.duchung.vn.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, UserDTO> implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        super(userRepository, userMapper, "User");
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDTO createUser(UserCreateRequest request) {
        // Tạo username từ fullname nếu chưa được cung cấp
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            request.setUsername(generateUsername(request.getFullName()));
        } else if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Đợi chốt rule

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    /**
     * Tạo username từ fullname theo format: [tên][chữ cái đầu họ][chữ cái đầu tên
     * đệm][số]
     * Ví dụ: Bùi Văn Mạnh -> manhbv1
     */
    private String generateUsername(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new BadRequestException("Full name is required to generate username");
        }

        // Chuẩn hóa fullname: loại bỏ dấu, chuyển thành chữ thường
        String normalizedName = removeAccents(fullName.toLowerCase());

        // Tách các phần của tên
        String[] nameParts = normalizedName.trim().split("\\s+");
        if (nameParts.length == 0) {
            throw new BadRequestException("Invalid full name format");
        }

        // Lấy tên (phần cuối cùng)
        String lastName = nameParts[nameParts.length - 1];

        // Tạo phần prefix từ chữ cái đầu của họ và tên đệm (nếu có)
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < nameParts.length - 1; i++) {
            if (!nameParts[i].isEmpty()) {
                prefix.append(nameParts[i].charAt(0));
            }
        }

        // Tạo username cơ bản: tên + chữ cái đầu của họ và tên đệm
        String baseUsername = lastName + prefix.toString();

        // Tìm số tiếp theo cho username
        int counter = 1;
        String username = baseUsername + counter;

        // Kiểm tra xem username đã tồn tại chưa và tăng counter nếu cần
        while (userRepository.existsByUsername(username)) {
            counter++;
            username = baseUsername + counter;
        }

        return username;
    }

    /**
     * Loại bỏ dấu từ chuỗi tiếng Việt
     */
    private String removeAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // email update được ko ta
        // if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())
        // &&
        // userRepository.existsByEmail(request.getEmail())) {
        // throw new BadRequestException("Email already exists");
        // }

        User updatedUser = userMapper.updateFromRequest(user, request);
        User savedUser = userRepository.save(updatedUser);
        return userMapper.toDto(savedUser);
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUsernameAndActive(username, true)
                .map(userMapper::toDto);
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmailAndActive(email, true)
                .map(userMapper::toDto);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<UserDTO> findByRole(RoleType role) {
        return userRepository.findByRoleAndActive(role, true)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> findBySearchCriteria(
            String fullName,
            String email,
            String username,
            RoleType role,
            String school,
            String phone,
            LocalDate fromDate,
            LocalDate toDate,
            Boolean active,
            Pageable pageable) {
        return userRepository.findAll(
                UserSpecification.buildSpecification(
                        fullName, email, username, role, school, phone, fromDate, toDate, active),
                pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional
    public void changePassword(Long id, String currentPassword, String newPassword) {
        User user = userRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}