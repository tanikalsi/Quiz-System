package com.quizsystem.quiz_service.controller;

import com.quizsystem.quiz_service.model.Question;
import com.quizsystem.quiz_service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz/{quizId}/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    // POST - Add question to a quiz
    @PostMapping
    public Question addQuestion(@PathVariable Long quizId, @RequestBody Question question) {
        return questionService.addQuestionToQuiz(quizId, question);
    }

    // GET - Get all questions for a quiz
    @GetMapping
    public List<Question> getQuestions(@PathVariable Long quizId) {
        return questionService.getQuestionsByQuiz(quizId);
    }
}
