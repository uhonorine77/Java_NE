package rw.ne.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.ne.java.model.Employee;
import rw.ne.java.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByEmployee(Employee employee);
    List<Message> findByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
    List<Message> findByEmployeeAndStatus(Employee employee, Message.MessageStatus status);
}