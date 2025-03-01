package com.duchung.vn.utils;

import com.duchung.vn.exception.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FileUtils {

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");
    private static final List<String> ALLOWED_DOCUMENT_EXTENSIONS = Arrays.asList("pdf", "doc", "docx", "xls", "xlsx",
            "ppt", "pptx");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }

        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1) {
            return ""; // No extension found
        }

        return fileName.substring(lastIndex + 1).toLowerCase();
    }

    public static boolean isImage(String fileName) {
        String extension = getFileExtension(fileName);
        return ALLOWED_IMAGE_EXTENSIONS.contains(extension);
    }

    public static boolean isDocument(String fileName) {
        String extension = getFileExtension(fileName);
        return ALLOWED_DOCUMENT_EXTENSIONS.contains(extension);
    }

    public static void validateFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds maximum limit of 10MB");
        }
    }

    public static void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("File cannot be empty");
        }

        if (!isImage(file.getOriginalFilename())) {
            throw new BadRequestException("Only image files are allowed (jpg, jpeg, png, gif)");
        }

        validateFileSize(file);
    }

    public static void validateDocumentFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("File cannot be empty");
        }

        if (!isDocument(file.getOriginalFilename())) {
            throw new BadRequestException("Only document files are allowed (pdf, doc, docx, xls, xlsx, ppt, pptx)");
        }

        validateFileSize(file);
    }

    public static String saveFile(MultipartFile file, String uploadDir) throws IOException {
        if (file.isEmpty()) {
            throw new BadRequestException("File cannot be empty");
        }

        validateFileSize(file);

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString() + "." + fileExtension;

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }

    public static boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }

    public static byte[] readFileToBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public static String getFileSizeDescription(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
        }
    }
}