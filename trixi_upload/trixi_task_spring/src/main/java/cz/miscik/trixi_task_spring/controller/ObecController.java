package cz.miscik.trixi_task_spring.controller;

import cz.miscik.trixi_task_spring.Service.ObecService;
import cz.miscik.trixi_task_spring.dto.CastObceDto;
import cz.miscik.trixi_task_spring.dto.ObecDto;
import org.springframework.web.bind.annotation.*;

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

// --------------------------------------------------------------------------------------------------
// GET operations --------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------

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

// --------------------------------------------------------------------------------------------------
// POST operations --------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------

    /**
     * Creates a new village using the provided data.
     *
     * @param dto data transfer object containing village information
     * @return success message upon successful insertion
     */
    @PostMapping
    public String postObec(@RequestBody ObecDto dto) { // @RequestBody => spring will automatically convert the JSON body of the request into an ObecDto object
        obecService.postObec(dto);
        return "Data Inserted Successfully";
    }

    /**
     * Creates a new part of a village using the provided data.
     *
     * @param dto data transfer object containing village part information
     * @return success message upon successful insertion
     */
    @PostMapping("/{kod}/casti")
    public String postCastObce(@RequestBody CastObceDto dto) {
        obecService.postCastObce(dto);
        return "Data Inserted Successfully";
    }

// --------------------------------------------------------------------------------------------------
// PUT operations --------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------

    /**
     * Updates an existing village identified by its code using the provided data.
     *
     * @param kod village code
     * @param dto data transfer object containing updated village information
     * @return success message upon successful update
     */
    @PutMapping("/{kod}")
    public String putObec(@PathVariable("kod") Integer kod, @RequestBody ObecDto dto) {
        ObecDto obec = obecService.putObec(kod, dto); // this is not a typo, just a Service method that returns ObecDto because of CashePut
        return "Data Updated Successfully";
    }

    /**
     * Updates an existing part of a village identified by the village code and part code using the provided data.
     *
     * @param kod village code
     * @param castKod part code
     * @param dto data transfer object containing updated village part information
     * @return success message upon successful update
     */
    @PutMapping("/{kod}/casti/{castKod}")
    public String putCastObce(@PathVariable("kod") Integer kod, @PathVariable("castKod") Integer castKod, @RequestBody CastObceDto dto) {
        CastObceDto castObce = obecService.putCastObce(kod, castKod, dto); // this is not a typo, just a Service method that returns ObecDto because of CashePut
        return "Data Updated Successfully";
    }

// --------------------------------------------------------------------------------------------------
// DELETE operations --------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------

    /**
     * Deletes an existing village identified by its code.
     *
     * @param kod village code
     * @return success message upon successful deletion
     */
    @DeleteMapping("/{kod}")
    public String deleteObec(@PathVariable("kod") Integer kod) {
        obecService.deleteObec(kod);
        return "Data Deleted Successfully";
    }

    /**
     * Deletes an existing part of a village identified by the village code and part code.
     *
     * @param kod village code
     * @param castKod part code
     * @return success message upon successful deletion
     */
    @DeleteMapping("/{kod}/casti/{castKod}")
    public String deleteCastObce(@PathVariable("kod") Integer kod, @PathVariable("castKod") Integer castKod) {
        obecService.deleteCastObce(kod, castKod);
        return "Data Deleted Successfully";
    }

}
