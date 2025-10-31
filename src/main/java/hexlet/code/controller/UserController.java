package hexlet.code.controller;


import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
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
@AllArgsConstructor
public class UserController {
//    private static final String ONLY_OWNER_BY_ID = """
//                @userRepository.findById(#id).get().getEmail() == authentication.getName()
//            """;


    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * @return List<UserDTO>
     */
    @GetMapping(path = "")
//    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDTO>> index() {
        var result = userService.getAll();
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(result.size())).body(result);
    }

    /**
     * @param id
     * @return UserDTO
     */
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO show(@PathVariable long id) {
        return userService.getById(id);
    }


    /**
     * @param dto
     * @return UserDTO
     */
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO dto) {
        return userService.create(dto);
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
        return userService.update(id, dto);
    }

    /**
     * @param id
     */
   // @PreAuthorize(ONLY_OWNER_BY_ID)
    @PreAuthorize("@userRepository.findById(#id).get().getEmail() == authentication.getName()")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }


}
