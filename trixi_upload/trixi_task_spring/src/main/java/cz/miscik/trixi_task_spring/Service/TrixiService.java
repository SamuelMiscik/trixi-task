package cz.miscik.trixi_task_spring.Service;

import cz.miscik.trixi_task_spring.entities.CastObce;
import cz.miscik.trixi_task_spring.entities.Obec;
import cz.miscik.trixi_task_spring.repository.CastObceRepository;
import cz.miscik.trixi_task_spring.repository.ObecRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TrixiService class is responsible for handling the business logic related to importing data into the database.
 * It uses repositories to save Obec and CastObce entities.
 */
// Exactly like Service in JPA+Hibernate but it uses some new syntax and is generally easier to use.
@Service // Indicates that this class is a service component in the Spring context. It is a specialization of the @Component annotation, allowing for automatic detection through classpath scanning.
public class TrixiService {

    private final ObecRepository obecRepository;
    private final CastObceRepository castObceRepository;

    /**
     * Constructor for TrixiService.
     *
     * @param obecRepository      repository for Obec entities
     * @param castObceRepository   repository for CastObce entities
     */
    public TrixiService(
            ObecRepository obecRepository,
            CastObceRepository castObceRepository) {
        this.obecRepository = obecRepository;
        this.castObceRepository = castObceRepository;
    }

    /**
     * Imports data into the database by saving lists of Obec and CastObce entities.
     *
     * @param villages          list of Obec entities to be saved
     * @param partsOfVillages   list of CastObce entities to be saved
     */
    @Transactional // Indicates that the method should be executed within a transaction.
    public void importData(List<Obec> villages, List<CastObce> partsOfVillages) { // Method to import data into the database.
        // save obce
        obecRepository.saveAll(villages); // replaces the loop with a simple function and saves every element in arraylist  into database
        // save casti obce
        castObceRepository.saveAll(partsOfVillages);// replaces the loop with a simple function and saves every element in arraylist  into database
    }
}
