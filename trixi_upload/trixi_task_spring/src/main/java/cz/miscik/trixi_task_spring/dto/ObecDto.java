package cz.miscik.trixi_task_spring.dto;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) for Obec entity.
 * This record is used to transfer data between layers of the application without exposing the entity directly.
 * It contains only the necessary fields to represent an Obec.
 */
public record ObecDto(
        Integer kod,
        String name
) implements Serializable { // implements seriazable because it is used in hazelcast caching and wont work without it

}
