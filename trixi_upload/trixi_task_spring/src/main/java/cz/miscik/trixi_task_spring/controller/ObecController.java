package cz.miscik.trixi_task_spring.controller;

import cz.miscik.trixi_task_spring.Service.ObecService;
import cz.miscik.trixi_task_spring.dto.CastObceDto;
import cz.miscik.trixi_task_spring.dto.ObecDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Controller class for handling REST API requests related to "Obec" and "CastObce" entities.
 *
 * <p>Handles requests under the {@code /api/obce} path and delegates
 * application logic to {@link ObecService}.</p>
 */
@RestController
@RequestMapping("/api/obce") // Only these REST API requests will be handled by this controller
public class ObecController {

    private final ObecService obecService;

    /**
     * Constructor : Creates a new controller using the given village service.
     *
     * @param obecService service providing village-related operations
     */
    public ObecController(ObecService obecService) {
        this.obecService = obecService;
    }


// GET operations --------------------------------------------------------------------------------------------------

    /**
     * Returns all villages.
     *
     * @return list of all villages represented as DTOs
     */
    @GetMapping
    public List<ObecDto> getAllObce() {
        return obecService.getAllObce();
    }

    /**
     * Returns a village identified by its code.
     *
     * @param kod village code
     * @return village represented as a DTO
     */
    @GetMapping("/{kod}")
    public ObecDto getObec(@PathVariable("kod") Integer kod) { // Gets kod as a variable into input
        return obecService.getObec(kod);
    }

    /**
     * Returns all parts belonging to the village identified by its code.
     *
     * @param kod village code
     * @return list of village parts represented as DTOs
     */
    @GetMapping("/{kod}/casti")
    public List<CastObceDto> getCastiObce(@PathVariable("kod") Integer kod) { // Gets kod as a variable into input
        return obecService.getCastiObce(kod);
    }

    /**
     * Returns a specific part of a village identified by the village code and part code.
     *
     * @param kod village code
     * @param castKod part code
     * @return specific village part represented as a DTO
     */
    @GetMapping("/{kod}/casti/{castKod}")
    public CastObceDto getCastObce(@PathVariable("kod") Integer kod, @PathVariable("castKod") Integer castKod) { // Gets kod as a variable into input
        return obecService.getCastObce(kod, castKod);
    }


}
