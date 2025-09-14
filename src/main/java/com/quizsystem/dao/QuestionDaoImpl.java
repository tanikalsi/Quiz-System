package com.quizsystem.dao;

import com.quizsystem.model.Question;
import com.quizsystem.model.Quiz; // <-- The fix is here
import org.springframework.orm.hibernate5.HibernateTemplate;
import javax.transaction.Transactional;

public class QuestionDaoImpl implements QuestionDao {

    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    @Transactional
    public void save(Question question) {
        this.hibernateTemplate.save(question);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Question question = findById(id);
        if (question != null) {
            Quiz parentQuiz = question.getQuiz();
            if (parentQuiz != null) {
                parentQuiz.getQuestions().remove(question);
            }
            this.hibernateTemplate.delete(question);
        }
    }

    @Override
    public Question findById(Long id) {
        return this.hibernateTemplate.get(Question.class, id);
    }
}