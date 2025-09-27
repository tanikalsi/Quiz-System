package com.quizsystem.quiz_service.service;

import com.quizsystem.quiz_service.model.Quiz;
import com.quizsystem.quiz_service.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }
    public Quiz createQuiz(Quiz quiz) {
        if (quiz.getQuestions() != null) {
            quiz.getQuestions().forEach(q -> q.setQuiz(quiz));  // har question ko quiz link karo
        }
        return quizRepository.save(quiz);
    }

}
