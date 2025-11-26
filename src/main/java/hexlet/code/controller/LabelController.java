package hexlet.code.controller;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.service.LabelServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
@AllArgsConstructor
public class LabelController {
    private final LabelServiceImpl labelServiceImpl;

    /**
     *
     * @return List
     */
    @GetMapping(path = "")
    public ResponseEntity<List<LabelDTO>> index() {
        var result = labelServiceImpl.getAll();
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(result.size())).body(result);
    }

    /**
     *
     * @param id
     * @return LabelDTO
     */
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO show(@PathVariable long id) {
        return labelServiceImpl.getById(id);
    }

    /**
     *
     * @param dto
     * @return LabelDTO
     */
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@Valid @RequestBody LabelCreateDTO dto) {
        return labelServiceImpl.create(dto);
    }

    /**
     *
     * @param id
     * @param dto
     * @return LabelDTO
     */
    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO update(@PathVariable("id") Long id, @Valid @RequestBody LabelUpdateDTO dto) {
        return labelServiceImpl.update(id, dto);
    }

    /**
     *
     * @param id
     */
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        labelServiceImpl.delete(id);
    }
}
