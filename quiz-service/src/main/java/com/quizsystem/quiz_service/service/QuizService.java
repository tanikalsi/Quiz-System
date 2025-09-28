package com.quizsystem.quiz_service.service;

import com.quizsystem.quiz_service.model.Quiz;
import com.quizsystem.quiz_service.model.Question;
import com.quizsystem.quiz_service.model.Result;
import com.quizsystem.quiz_service.model.Submission;
import com.quizsystem.quiz_service.repository.QuizRepository;
import com.quizsystem.quiz_service.repository.ResultRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private ResultRepository resultRepository;

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Quiz createQuiz(Quiz quiz) {
        if (quiz.getQuestions() != null) {
            quiz.getQuestions().forEach(q -> q.setQuiz(quiz));  // link each question with quiz
        }
        return quizRepository.save(quiz);
    }

    public Result calculateScore(Long quizId, Submission submission) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        int score = 0;
        for (Question question : quiz.getQuestions()) {
            String correctAnswer = question.getCorrectAnswer();
            String userAnswer = submission.getAnswers().get(question.getId());
            if (correctAnswer != null && correctAnswer.equals(userAnswer)) {
                score++;
            }
        }

        Result result = new Result();
        result.setQuizId(quizId);
        result.setUserId(submission.getUserId());
        result.setScore(score);
        result.setTotalQuestions(quiz.getQuestions().size());
        result.setAttemptedOn(java.time.LocalDateTime.now());

        return resultRepository.save(result);
    }
}
