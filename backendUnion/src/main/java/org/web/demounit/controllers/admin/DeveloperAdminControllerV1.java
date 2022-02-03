package org.web.demounit.controllers.admin;

import org.web.demounit.persists.entities.Developer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/admin/developers")
public class DeveloperAdminControllerV1 {
    private List<Developer> DEVELOPERS = Stream.of(
            new Developer(1L, "Boris", "Bruce"),
            new Developer(2L, "Leha", "Leshev"),
            new Developer(3L, "Vasilich", "Vasilichev")
    ).collect(Collectors.toList());

    @GetMapping
    public List<Developer> getAll() {
        return DEVELOPERS;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public Developer getById(@PathVariable Long id) {
        return DEVELOPERS.stream().filter(developer -> developer.getId().equals(id))
        .findFirst()
        .orElse(null);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('write')")
    public Developer create(@RequestBody Developer developer) {
        this.DEVELOPERS.add(developer);
        return developer;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public void delete(@PathVariable Long id) {
        this.DEVELOPERS.removeIf(developer -> developer.getId().equals(id));
    }
}
