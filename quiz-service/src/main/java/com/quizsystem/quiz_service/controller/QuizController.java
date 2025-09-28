package com.quizsystem.quiz_service.controller;

import com.quizsystem.quiz_service.model.Quiz;
import com.quizsystem.quiz_service.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.quizsystem.quiz_service.model.Result;
import com.quizsystem.quiz_service.model.Submission; // <-- CORRECT IMPORT ADDED

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    // GET - saare quizzes
    @GetMapping
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    // POST - new quiz create
    @PostMapping
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        return quizService.createQuiz(quiz);
    }

    @PostMapping("/{quizId}/submit")
    public Result submitQuiz(@PathVariable Long quizId, @RequestBody Submission submission) {
        // Now this 'Submission' refers to the correct class from the model package
        return quizService.calculateScore(quizId, submission);
    }

    // THE INNER SUBMISSION CLASS HAS BEEN DELETED FROM HERE
}