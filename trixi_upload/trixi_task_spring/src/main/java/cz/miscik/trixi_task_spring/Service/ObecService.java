package cz.miscik.trixi_task_spring.Service;

import cz.miscik.trixi_task_spring.dto.CastObceDto;
import cz.miscik.trixi_task_spring.dto.ObecDto;
import cz.miscik.trixi_task_spring.entities.CastObce;
import cz.miscik.trixi_task_spring.entities.Obec;
import cz.miscik.trixi_task_spring.repository.CastObceRepository;
import cz.miscik.trixi_task_spring.repository.ObecRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


/**
 * Service providing operations related to villages and village parts.
 *
 * <p>Uses repositories to access entities and maps them to DTOs
 * returned by the REST layer.</p>
 */
@Service
public class ObecService {

    private final ObecRepository obecRepository;
    private final CastObceRepository castObceRepository;

    /**
     * Constructor : Creates a new service using the given repositories.
     *
     * @param obecRepository repository for accessing village entities
     * @param castObceRepository repository for accessing village part entities
     */
    public ObecService(ObecRepository obecRepository, CastObceRepository castObceRepository) {
        this.obecRepository = obecRepository;
        this.castObceRepository = castObceRepository;
    }

    /**
     * Returns all villages stored in the database.
     *
     * @return list of villages represented as DTOs
     */
    public List<ObecDto> getAllObce() {
        List<Obec> obce = obecRepository.findAll();

        // InteliJ suggested this and it SO SMART -> I would have used obce = Entity object BUT thats not what we want so we map it to ObecDto object and return that instead of Entity object
        return obce.stream()
                .map(obec -> new ObecDto( // for every obec make a new ObecDto
                        obec.getKod(), //add kod and name to the new ObecDto object
                        obec.getName()
                ))
                .toList(); // ofc we need a list

// Easier way to do it without functional programming
//        for (Obec obec : obce) {
//            ObecDto obecDto = new ObecDto(obec.getKod(), obec.getName());
    }

    /**
     * Finds a village by its code and maps it to a DTO.
     *
     * @param kod village code
     * @return village represented as a DTO, or {@code null} if it does not exist
     */
    public ObecDto getObec(Integer kod) {
        Obec obec = obecRepository.findById(kod).orElse(null);

        if (obec == null) {
            return null;
        }

        return new ObecDto(
                obec.getKod(),
                obec.getName()
        );
    }

    /**
     * Returns all parts belonging to the village identified by the given code.
     *
     * @param kod village code
     * @return list of village parts represented as DTOs
     * @throws java.util.NoSuchElementException if the village does not exist
     */
    public List<CastObceDto> getCastiObce(Integer kod) {
        Obec obec = obecRepository.findById(kod).orElseThrow(); // orElseThrow because it returns Optional => if there is nothing it returns null and we dont want that
        List<CastObce> castiObce = obec.getCastiObce();

        // InteliJ suggested this and it SO SMART -> I would have used obce = Entity object BUT thats not what we want so we map it to ObecDto object and return that instead of Entity object
        return castiObce.stream()
                .map(castObce -> new CastObceDto( // for every obec make a new ObecDto
                        castObce.getKod(), //add kod and name to the new ObecDto object
                        castObce.getName(),
                        castObce.getObec().getKod()
                ))
                .toList(); // ofc we need a list
    }

    /**
     * Finds a specific village part belonging to the given village.
     *
     * @param kod village code
     * @param castKod village-part code
     * @return village part represented as a DTO
     * @throws ResponseStatusException if the village part cannot be found
     */
    public CastObceDto getCastObce(Integer kod, Integer castKod) {
        Obec obec = obecRepository.findById(kod).orElseThrow(); // orElseThrow because it returns Optional => if there is nothing it returns null and we dont want that
        List<CastObce> castiObce = obec.getCastiObce();

        for (CastObce castObce : castiObce) {
            if(castObce.getKod().equals(castKod)) { // Equals because we use Integer object and not int
                CastObceDto castObceDto = new CastObceDto(castObce.getKod(), castObce.getName(), castObce.getObec().getKod());
                return castObceDto;
            }
        }
        // If we dont find the right one we tell spring to return 404 NOT FOUND
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Cast obce not found"
        );
    }

}
