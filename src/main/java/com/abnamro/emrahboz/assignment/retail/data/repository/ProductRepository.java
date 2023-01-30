package com.abnamro.emrahboz.assignment.retail.data.repository;

import com.abnamro.emrahboz.assignment.retail.data.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByNameContaining(String name);

    Optional<ProductEntity> findTopByLastUpdateLessThan(LocalDateTime lastUpdate);

}
