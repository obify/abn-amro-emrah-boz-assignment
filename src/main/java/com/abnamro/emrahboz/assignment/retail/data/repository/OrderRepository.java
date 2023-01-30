package com.abnamro.emrahboz.assignment.retail.data.repository;

import com.abnamro.emrahboz.assignment.retail.data.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findById(String id);

    @Query(value = "select sum(order_total_amount) as order_total_amount, " +
            "to_char(last_update, 'yyyy-mm-dd')  as day " +
            "from ORDERS " +
            "where last_update >= :startDate and last_update <= :endDate " +
            " and status = 1 " +
            "group by to_char(last_update, 'yyyy-mm-dd') ", nativeQuery = true)
    List<Tuple> findSalesAmount(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<OrderEntity> findByLastUpdateLessThan(LocalDateTime lastUpdate);

}
