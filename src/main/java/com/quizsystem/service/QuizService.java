package com.quizsystem.service;

import com.quizsystem.dao.QuestionDao;
import com.quizsystem.dao.QuizDao;
import com.quizsystem.model.Question;
import com.quizsystem.model.Quiz;
import java.util.List;

public class QuizService {
    private QuizDao quizDao;
    private QuestionDao questionDao;

    // Setters for Spring dependency injection
    public void setQuizDao(QuizDao quizDao) {
        this.quizDao = quizDao;
    }
    public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    // --- Feature Implementations ---

    public Quiz createQuiz(Quiz quiz) {
        Long id = quizDao.save(quiz);
        return quizDao.findById(id);
    }

    public Question addQuestionToQuiz(Long quizId, Question question) {
        Quiz quiz = quizDao.findById(quizId);
        if (quiz == null) {
            throw new RuntimeException("Quiz not found with id " + quizId);
        }
        question.setQuiz(quiz);
        questionDao.save(question);
        return question;
    }

    public List<Quiz> getAllQuizzes() {
        return quizDao.findAll();
    }

    public Quiz getQuizWithQuestions(Long quizId) {
        return quizDao.findById(quizId);
    }

    public void deleteQuiz(Long quizId) {
        quizDao.delete(quizId);
    }

    public void deleteQuestion(Long questionId) {
        questionDao.delete(questionId);
    }
}