package com.circule.talent.controllers.api;

import com.circule.talent.dto.users.UserCreateDTO;
import com.circule.talent.dto.users.UserDTO;
import com.circule.talent.dto.users.UserUpdateDTO;
import com.circule.talent.service.UserService;
import com.circule.talent.utils.UserUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserUtils userUtils;

    @GetMapping(path = "")
    public ResponseEntity<List<UserDTO>> index() {
        var result = userService.index();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    @GetMapping(path = "/{id}")
    public UserDTO show(@PathVariable long id) {
        return userService.show(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO userData) {
        return userService.create(userData);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("@userUtils.isCurrentUser(#id)")
    public UserDTO update(@Valid @RequestBody UserUpdateDTO userData, @PathVariable Long id) {
        return userService.update(userData, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@userUtils.isCurrentUser(#id)")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
