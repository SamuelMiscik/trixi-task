package cz.miscik.trixi_task_spring.dto;

/**
 * Data Transfer Object (DTO) for CastObce entity.
 * This record is used to transfer data between layers of the application without exposing the entity directly.
 * It contains only the necessary fields to represent a CastObce.
 */
public record CastObceDto(
        Integer kod,
        String name,
        Integer obecKod
) {}
