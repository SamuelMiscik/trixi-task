package cz.miscik.trixi_task_spring.Service;

import cz.miscik.trixi_task_spring.entities.CastObce;
import cz.miscik.trixi_task_spring.entities.Obec;
import cz.miscik.trixi_task_spring.repository.CastObceRepository;
import cz.miscik.trixi_task_spring.repository.ObecRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

// Exactly like Service in JPA+Hibernate but it uses some new syntax and is generally easier to use.
@Service // Indicates that this class is a service component in the Spring context. It is a specialization of the @Component annotation, allowing for automatic detection through classpath scanning.
public class TrixiService {

    private final ObecRepository obecRepository;
    private final CastObceRepository castObceRepository;

    public TrixiService(
            ObecRepository obecRepository,
            CastObceRepository castObceRepository) {
        this.obecRepository = obecRepository;
        this.castObceRepository = castObceRepository;
    }

    @Transactional // Indicates that the method should be executed within a transaction.
    public void importData(List<Obec> villages, List<CastObce> partsOfVillages) { // Method to import data into the database.
        // save obce
        obecRepository.saveAll(villages); // replaces the loop with a simple function and saves every element in arraylist  into database
        // save casti obce
        castObceRepository.saveAll(partsOfVillages);// replaces the loop with a simple function and saves every element in arraylist  into database
    }
}
