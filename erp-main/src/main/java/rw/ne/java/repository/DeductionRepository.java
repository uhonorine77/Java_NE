package rw.ne.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.ne.java.model.Deduction;

import java.util.Optional;

@Repository
public interface DeductionRepository extends JpaRepository<Deduction, String> {
    Optional<Deduction> findByDeductionName(String deductionName);
    boolean existsByDeductionName(String deductionName);
}