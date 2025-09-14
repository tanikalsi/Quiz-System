package com.quizsystem.dao;

import com.quizsystem.model.Quiz;
import java.util.List;

public interface QuizDao {
    Long save(Quiz quiz);
    Quiz findById(Long id);
    List<Quiz> findAll();
    void delete(Long id);
}