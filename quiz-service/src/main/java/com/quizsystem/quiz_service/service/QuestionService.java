package com.quizsystem.quiz_service.service;

import com.quizsystem.quiz_service.model.Question;
import com.quizsystem.quiz_service.model.Quiz;
import com.quizsystem.quiz_service.repository.QuestionRepository;
import com.quizsystem.quiz_service.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    public Question addQuestionToQuiz(Long quizId, Question question) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        question.setQuiz(quiz);
        return questionRepository.save(question);
    }

    public List<Question> getQuestionsByQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        return questionRepository.findByQuiz(quiz);
    }
}
