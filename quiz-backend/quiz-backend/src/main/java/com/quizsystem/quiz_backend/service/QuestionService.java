package com.quizsystem.quiz_backend.service;

import com.quizsystem.quiz_backend.model.Question;
import com.quizsystem.quiz_backend.model.Quiz;
import com.quizsystem.quiz_backend.repository.QuestionRepository;
import com.quizsystem.quiz_backend.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;
    public void deleteQuestion(Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new RuntimeException("Question not found with id " + questionId);
        }
        questionRepository.deleteById(questionId);
    }


    public Question addQuestionToQuiz(Long quizId, Question question) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id " + quizId));

        question.setQuiz(quiz);
        return questionRepository.save(question);
    }

    public List<Question> getQuestionsByQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id " + quizId));

        return quiz.getQuestions();
    }
}
