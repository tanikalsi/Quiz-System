package com.quizsystem.quiz_backend.controller;

import com.quizsystem.quiz_backend.model.Question;
import com.quizsystem.quiz_backend.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz/{quizId}/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    // POST - Add Question to Quiz
    @PostMapping
    public Question addQuestion(@PathVariable Long quizId, @RequestBody Question question) {
        return questionService.addQuestionToQuiz(quizId, question);
    }

    // GET - Get all questions for a quiz
    @GetMapping
    public List<Question> getQuestions(@PathVariable Long quizId) {
        return questionService.getQuestionsByQuiz(quizId);
    }
    // DELETE - Delete a question by ID
    @DeleteMapping("/{questionId}")
    public String deleteQuestion(@PathVariable Long quizId, @PathVariable Long questionId) {
        questionService.deleteQuestion(questionId);
        return "Question with id " + questionId + " deleted successfully.";
    }

}
