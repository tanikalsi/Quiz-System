package com.quizsystem.dao;

import com.quizsystem.model.Quiz;
import org.springframework.orm.hibernate5.HibernateTemplate;
import javax.transaction.Transactional;
import java.util.List;

public class QuizDaoImpl implements QuizDao {

    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    @Transactional
    public Long save(Quiz quiz) {
        return (Long) this.hibernateTemplate.save(quiz);
    }

    @Override
    public Quiz findById(Long id) {
        return this.hibernateTemplate.get(Quiz.class, id);
    }

    @Override
    public List<Quiz> findAll() {
        return this.hibernateTemplate.loadAll(Quiz.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Quiz quiz = findById(id);
        if (quiz != null) {
            this.hibernateTemplate.delete(quiz);
        }
    }
}