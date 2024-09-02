package com.appliances.recyle.repository;

import com.appliances.recyle.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}