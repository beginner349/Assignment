package sg.com.nphc.Assignment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sg.com.nphc.Assignment.model.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    List<Employee> findBySalaryGreaterThanEqualAndSalaryLessThan(double minSalary, double maxSalary, Pageable pageable);

    boolean existsByLogin(String login);

    Employee findByLogin(String login);
}
