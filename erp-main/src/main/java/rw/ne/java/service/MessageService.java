package rw.ne.java.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import rw.ne.java.model.Employee;
import rw.ne.java.model.Message;
import rw.ne.java.model.Payslip;
import rw.ne.java.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final JavaMailSender mailSender;

    public MessageService(MessageRepository messageRepository, JavaMailSender mailSender) {
        this.messageRepository = messageRepository;
        this.mailSender = mailSender;
    }

    public Message createPaymentMessage(Payslip payslip) {
        Employee employee = payslip.getEmployee();
        String content = generatePaymentMessageContent(payslip);

        Message message = new Message();
        message.setEmployee(employee);
        message.setContent(content);
        message.setMonth(payslip.getMonth());
        message.setYear(payslip.getYear());
        message.setStatus(Message.MessageStatus.SENT);
        message.setCreatedAt(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);

        // Send email notification
        sendEmailNotification(employee.getEmail(), "Salary Credited", content);

        return savedMessage;
    }

    private String generatePaymentMessageContent(Payslip payslip) {
        Employee employee = payslip.getEmployee();
        return "Dear " + employee.getFirstName() + ", " +
                "Your salary of " + payslip.getMonth() + "/" + payslip.getYear() + 
                " from Rwanda Government " + payslip.getNetSalary() + 
                " RWF has been credited to your " + employee.getCode() + " account successfully.";
    }

    private void sendEmailNotification(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
        } catch (Exception e) {
            // Log the error but don't throw exception to prevent transaction rollback
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }

    public List<Message> getMessagesByEmployee(Employee employee) {
        return messageRepository.findByEmployee(employee);
    }

    public List<Message> getMessagesByEmployeeAndMonthYear(Employee employee, Integer month, Integer year) {
        return messageRepository.findByEmployeeAndMonthAndYear(employee, month, year);
    }
}
