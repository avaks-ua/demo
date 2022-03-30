package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

@Slf4j
@RequiredArgsConstructor
@RestController
class UserController {

    private final UserRepository repo;

    @PostMapping("/user")
    public User postUser(@RequestBody User user) {
        return repo.save(user);
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        return getUserById(id);
    }

    private User getUserById(Integer id) {
        UserNotFoundException ex = new UserNotFoundException("User not found " + id);
        Optional<User> user = repo.findById(id);
        if (user.isEmpty()) {
            throw ex;
        }
        return user.get();
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return repo.findAll();
    }
}

@ControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomHttpResponse> handle(RuntimeException ex) {
        return new ResponseEntity<>(
                new CustomHttpResponse(ex.getMessage(), "404"),
                HttpStatus.NOT_FOUND
        );
    }

}

@AllArgsConstructor
@Data
class CustomHttpResponse {

    private String message;
    private String status;

}

class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}


@Repository
interface UserRepository extends JpaRepository<User, Integer> {


}

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_table")
class User {
    @Id
    @GeneratedValue
    private Integer id;
    @Column
    private String name;
    @Column
    private String email;
}
