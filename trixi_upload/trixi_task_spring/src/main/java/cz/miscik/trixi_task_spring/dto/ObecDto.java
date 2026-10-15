package cz.miscik.trixi_task_spring.dto;

/**
 * Data Transfer Object (DTO) for Obec entity.
 * This record is used to transfer data between layers of the application without exposing the entity directly.
 * It contains only the necessary fields to represent an Obec.
 */
public record ObecDto(
        Integer kod,
        String name
) {}
