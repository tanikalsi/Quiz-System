package com.quizsystem.quiz_backend.repository;

import com.quizsystem.quiz_backend.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
