package com.quizsystem.quiz_service.controller;

import com.quizsystem.quiz_service.model.Result;
import com.quizsystem.quiz_service.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/results")
public class ResultController {

    @Autowired
    private ResultRepository resultRepository;

    @GetMapping("/user/{userId}")
    public List<Result> getResultsByUser(@PathVariable Long userId) {
        return resultRepository.findByUserId(userId);
    }
}