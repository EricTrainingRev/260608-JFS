package com.example.demo.repo;

import com.example.demo.entity.Book;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends JpaRepository<Book, UUID> {
  List<Book> findAllByUserId(UUID userId);

  Optional<Book> findByIdAndUserId(UUID id, UUID userId);
}
