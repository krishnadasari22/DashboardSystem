package com.example.demo.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.MyEntity;
import com.example.demo.service.MyService;

@RestController
@RequestMapping("/entities")
public class MyController {
    
    @Autowired
    private MyService service;
    
    @GetMapping
    public List<MyEntity> getAllEntities() {
        return service.getAllEntities();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MyEntity> getEntityById(@PathVariable Long id) {
        Optional<MyEntity> entity = service.getEntityById(id);
        return entity.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/newdata")
    public ResponseEntity<MyEntity> saveEntity(@RequestBody MyEntity entity) {
        MyEntity savedEntity = service.saveEntity(entity);
        return ResponseEntity.created(URI.create("/entities/" + savedEntity.getId())).body(savedEntity);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MyEntity> updateEntity(@PathVariable Long id, @RequestBody MyEntity entity) {
        Optional<MyEntity> optionalEntity = service.getEntityById(id);
        if (optionalEntity.isPresent()) {
            MyEntity existingEntity = optionalEntity.get();
            existingEntity.setIntensity(entity.getIntensity());
            existingEntity.setLikehood(entity.getLikehood());
            existingEntity.setRelevance(entity.getRelevance());
            existingEntity.setYear(entity.getYear());
            existingEntity.setCountry(entity.getCountry());
            existingEntity.setTopics(entity.getTopics());
            existingEntity.setRegion(entity.getRegion());
            existingEntity.setCity(entity.getCity());
            MyEntity updatedEntity = service.saveEntity(existingEntity);
            return ResponseEntity.ok(updatedEntity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Long id) {
        Optional<MyEntity> optionalEntity = service.getEntityById(id);
        if (optionalEntity.isPresent()) {
            service.deleteEntity(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

