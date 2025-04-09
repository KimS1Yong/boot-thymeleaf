package org.example.bootthymeleaf.model.repository;

import org.example.bootthymeleaf.model.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// pk 타입 주의하기 (uuid -> String)
@Repository
public interface WordRepository extends JpaRepository<Word, String> {
    List<Word> findAllByOrderByCreatedAtDesc();
}
