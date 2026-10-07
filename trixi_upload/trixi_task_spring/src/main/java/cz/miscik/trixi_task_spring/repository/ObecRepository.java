package cz.miscik.trixi_task_spring.repository;

import cz.miscik.trixi_task_spring.entities.Obec;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for CastObce entity, extending JpaRepository to provide CRUD operations
 * additional query methods can be defined here if needed like findByDateRange(...), findByOrderNumber(...), findByVin(...)
 * real class is not needed here because Spring Data JPA will automatically generate the implementation at runtime based on the method names and signatures
*/

public interface ObecRepository
        extends JpaRepository<Obec, Integer> {
}