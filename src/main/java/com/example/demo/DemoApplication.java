package com.example.demo;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

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
        return repo.findById(id).get();
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return repo.findAll();
    }
}


@Repository
interface UserRepository extends JpaRepository<User, Integer> {

    User finUserByName(String name);

}

@Data
@Entity
@Table(name = "user_table")
class User {
    @Id
    @GeneratedValue
    private Integer id;
    @Column
    private String name;
}