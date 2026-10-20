package cz.miscik.trixi_task_spring.Service;

import cz.miscik.trixi_task_spring.dto.CastObceDto;
import cz.miscik.trixi_task_spring.dto.ObecDto;
import cz.miscik.trixi_task_spring.entities.CastObce;
import cz.miscik.trixi_task_spring.entities.Obec;
import cz.miscik.trixi_task_spring.repository.CastObceRepository;
import cz.miscik.trixi_task_spring.repository.ObecRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


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

// --------------------------------------------------------------------------------------------------
// GET operations --------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------

    /**
     * Returns all villages stored in the database.
     *
     * @return list of villages represented as DTOs
     */
    @Cacheable(value = "allObce") // Cacheable because until something changes with the obce, it stays the same !any change of data = immediate evict of this cached data because its not accurate anymore!
    public List<ObecDto> getAllObce() {
        System.out.println("Getting all obce from database"); // This is just to show that the cache works, because if it works, this line will only be printed once and then the cache will be used for the next requests
        List<Obec> obce = obecRepository.findAll();

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
    @Cacheable(value = "obce", key = "#kod")
    public ObecDto getObec(Integer kod) {
        System.out.println("Getting obec from database");
        Obec obec = obecRepository.findById(kod)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, //  NOT_FOUND(404, HttpStatus.Series.CLIENT_ERROR, "Not Found"),
                        "Obec with kod " + kod + " not found"
                ));

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
    @Cacheable(value = "allCastiObce") //!any change of data = immediate evict of this cached data because its not accurate anymore!
    public List<CastObceDto> getCastiObce(Integer kod) {
        System.out.println("Getting all casti from database");
        Obec obec = obecRepository.findById(kod)
                .orElseThrow(() -> new ResponseStatusException( // orElseThrow because it returns Optional => if there is nothing it returns null and we dont want that
                        HttpStatus.NOT_FOUND,
                        "Obec not found"
                ));

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
    @Cacheable(value = "castiObce", key = "#kod + ':' + #castKod")
    public CastObceDto getCastObce(Integer kod, Integer castKod) {
        System.out.println("Getting cast from database");
        Obec obec = obecRepository.findById(kod)
                .orElseThrow(() -> new ResponseStatusException( // orElseThrow because it returns Optional => if there is nothing it returns null and we dont want that
                        HttpStatus.NOT_FOUND,
                        "Obec not found"
                ));

        List<CastObce> castiObce = obec.getCastiObce();

        for (CastObce castObce : castiObce) {
            if(castObce.getKod().equals(castKod)) { // Equals because we use Integer object and not int
                CastObceDto castObceDto = new CastObceDto(castObce.getKod(), castObce.getName(), castObce.getObec().getKod());
                return castObceDto;
            }
        }
        // If we don't find the right one we tell spring to return 404 NOT FOUND
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Cast obce not found"
        );
    }

// --------------------------------------------------------------------------------------------------
// POST operations --------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------

    /**
     * Creates a new village in the database using the provided DTO.
     *
     * @param dto DTO containing village data
     */
    @Caching(evict = {
            @CacheEvict(value = "allObce", allEntries = true), // Evicts the cache for all villages when a new village is created because the cache is not accurate anymore
            @CacheEvict(value = "obce", key = "#dto.kod()") // THIS is just because when you call THIS exact obec with this exact CODE it returns null and that might remain in cache so we wanna get rid of that possibility
    })
    public void postObec(ObecDto dto) {
        System.out.println("Posting new obec to database + evicting cache for allObce");
        Obec obec = new Obec();

        obec.setKod(dto.kod());
        obec.setName(dto.name());

        obecRepository.save(obec);
    }

    /**
     * Creates a new village part in the database using the provided DTO.
     *
     * @param dto DTO containing village part data
     * @throws ResponseStatusException if the parent village cannot be found
     */
    @Caching(evict = {
            @CacheEvict(value = "allCastiObce", key = "#dto.obecKod()"),
            @CacheEvict(value = "castiObce", key = "#dto.obecKod() + ':' + #dto.kod()")
    })
    public void postCastObce(CastObceDto dto) {
        System.out.println("Posting new castObce to database + evicting cache for allCastiObce");
        CastObce castObce = new CastObce();

        castObce.setKod(dto.kod());
        castObce.setName(dto.name());

        Obec obec = obecRepository.findById(dto.obecKod())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Obec not found"
                ));

        castObce.setObec(obec);

        castObceRepository.save(castObce);
    }

// --------------------------------------------------------------------------------------------------
// PUT operations --------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------

    /**
     * Updates an existing village in the database using the provided DTO.
     *
     * @param kod village code
     * @param dto DTO containing updated village data
     * @throws ResponseStatusException if the village cannot be found
     */
    @Caching(
            put = {
                    @CachePut(value = "obce", key = "#kod") // Changes the cache for the specific village when it is updated
            },
            evict = {
                    @CacheEvict(value = "allObce", allEntries = true) // Evicts the cache for all villages when a specific village is updated because the cache is not accurate anymore
            }
    )
    public ObecDto putObec(Integer kod, ObecDto dto) {
        System.out.println("Putting obec to database + changing cache for specific obec + evicting cache for allObce");
        Obec obec = obecRepository.findById(kod)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Obec not found"
                ));

//        obec.setKod(dto.kod()); // Identifier shouldnt change
        obec.setName(dto.name());

        obecRepository.save(obec);

        return new ObecDto(obec.getKod(), obec.getName()); //This is just so the CachePut works, otherwise method doesnt need it, but CachePut works here better then CacheEvict
    }

    /**
     * Updates an existing village part in the database using the provided DTO.
     *
     * @param kod village code
     * @param castKod village-part code
     * @param dto DTO containing updated village part data
     * @throws ResponseStatusException if the village or village part cannot be found
     */
    @Caching(
            put = {
                    @CachePut(value = "castiObce", key = "#kod + ':' + #castKod") // Changes the cache for the specific village part when it is updated
            },
            evict = {
                    @CacheEvict(value = "allCastiObce", key = "#kod") // Evicts only the cache for all village parts of the specific village when a specific village part is updated because the cache is not accurate anymore
            }
    )
    public CastObceDto putCastObce(Integer kod, Integer castKod, CastObceDto dto) {
        System.out.println("Putting castObce to database + changing cache for specific castObce + evicting cache for allCastiObce");
        Obec obec = obecRepository.findById(kod)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Obec not found"
                ));

        List<CastObce> castiObce = obec.getCastiObce();

        for (CastObce castObce : castiObce) {
            if(castObce.getKod().equals(castKod)) {
//                castObce.setKod(dto.kod()); // Identifier shouldnt change
                castObce.setName(dto.name());
                castObceRepository.save(castObce);
                return new CastObceDto(castObce.getKod(), castObce.getName(), castObce.getObec().getKod()); //This is just so the CachePut works, otherwise method doesnt need it, but CachePut works here better then CacheEvict
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Cast obce not found"
        );
    }

// --------------------------------------------------------------------------------------------------
// DELETE operations --------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------

    /**
     * Deletes an existing village from the database.
     *
     * @param kod village code
     * @throws ResponseStatusException if the village cannot be found
     */
    @Caching(
            evict = {
                    @CacheEvict(value = "obce", key = "#kod"), // Evicts the cache for the specific village when it is deleted
                    @CacheEvict(value = "allObce", allEntries = true) // Evicts the cache for all villages when a specific village is deleted because the cache is not accurate anymore
            }
    )
    public void deleteObec(Integer kod) {
        System.out.println("Deleting obec from database + evicting cache for specific obec + evicting cache for allObce");
        Obec obec = obecRepository.findById(kod)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Obec not found"
                ));

        obecRepository.delete(obec);
    }

    /**
     * Deletes an existing village part from the database.
     *
     * @param kod village code
     * @param castKod village-part code
     * @throws ResponseStatusException if the village or village part cannot be found
     */
    @Caching(
            evict = {
                    @CacheEvict(value = "castiObce", key = "#kod + ':' + #castKod"), // Evicts the cache for the specific village part when it is deleted
                    @CacheEvict(value = "allCastiObce", key = "#kod") // Evicts only the cache for all village parts of the specific village when a specific village part is deleted because the cache is not accurate anymore
            }
    )
    public void deleteCastObce(Integer kod, Integer castKod) {
        System.out.println("Deleting castObce from database + evicting cache for specific castObce + evicting cache for allCastiObce");
        Obec obec = obecRepository.findById(kod)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Obec not found"
                ));

        List<CastObce> castiObce = obec.getCastiObce();

        for (CastObce castObce : castiObce) {
            if(castObce.getKod().equals(castKod)) {
                castObceRepository.delete(castObce);
                return;
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Cast obce not found"
        );
    }


}
