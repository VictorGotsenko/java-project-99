package hexlet.code.controller;


import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hexlet.code.dto.user.UserDTO;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "API to manage user data")
@AllArgsConstructor
//@RequredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserServiceImpl userServiceImpl;

    /**
     * @return List<UserDTO>
     */
    @GetMapping(path = "")
//    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDTO>> index() {
        var result = userServiceImpl.getAll();
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(result.size())).body(result);
    }

    /**
     * @param id
     * @return UserDTO
     */
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO show(@PathVariable long id) {
        return userServiceImpl.getById(id);
    }


    /**
     * @param dto
     * @return UserDTO
     */
    @Operation(summary = "Create new user")
    @ApiResponse(responseCode = "201", description = "User was create")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO dto) {
        return userServiceImpl.create(dto);
    }

    /**
     * @param id
     * @param dto
     * @return UserDTO
     */
    @PreAuthorize("@userRepository.findById(#id).get().getEmail() == authentication.getName()")
    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateDTO dto) {
        return userServiceImpl.update(id, dto);
    }

    /**
     * @param id
     */
    // @PreAuthorize(ONLY_OWNER_BY_ID)
    @PreAuthorize("@userRepository.findById(#id).get().getEmail() == authentication.getName()")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        userServiceImpl.delete(id);
    }


}
