package sg.com.nphc.Assignment.service;

import org.springframework.web.multipart.MultipartFile;
import sg.com.nphc.Assignment.model.Employee;

import java.io.IOException;
import java.util.List;

public interface EmployeeService {
    boolean save(MultipartFile file) throws IOException;

    List<Employee> fetchEmployees(double minSalary, double maxSalary, int offset, int limit, String sortBy);

    Employee findById(String id);

    String save(Employee employee);

    String update(String id, Employee employee);

    String delete(String id);
}
