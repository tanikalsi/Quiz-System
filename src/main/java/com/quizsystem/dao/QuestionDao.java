package com.quizsystem.dao;

import com.quizsystem.model.Question;

public interface QuestionDao {
    void save(Question question);
    void delete(Long id);
    Question findById(Long id);
}