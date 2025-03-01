package com.duchung.vn.controller;

import com.duchung.vn.entity.BaseEntity;
import com.duchung.vn.service.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
public abstract class ApiController<T extends BaseEntity, D> {

    private final BaseService<T, D> service;

    @GetMapping
    @Operation(summary = "Get all entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entities retrieved successfully")
    })
    public ResponseEntity<List<D>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/page")
    @Operation(summary = "Get entities with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entities page retrieved successfully")
    })
    public ResponseEntity<Page<D>> getAllPaged(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get entity by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    public ResponseEntity<D> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entity created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<D> create(@Valid @RequestBody D dto) {
        return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    public ResponseEntity<D> update(@PathVariable Long id, @Valid @RequestBody D dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entity deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    @Operation(summary = "Hard delete an entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entity deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}