package sg.com.nphc.Assignment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sg.com.nphc.Assignment.exception.BadInputException;
import sg.com.nphc.Assignment.model.Employee;
import sg.com.nphc.Assignment.service.EmployeeService;

import java.io.IOException;
import java.util.List;

import static sg.com.nphc.Assignment.util.Helper.generateResponse;

@RestController
@RequestMapping("users")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("upload")
    public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file) throws IOException {
        boolean changed = employeeService.save(file);
        if (changed) {
            return generateResponse("Data created or uploaded.", HttpStatus.CREATED);
        } else {
            return generateResponse("Success but no data updated.", HttpStatus.OK);
        }
    }

    @GetMapping
    public List<Employee> fetchEmployees(
            @RequestParam(defaultValue = "0") double minSalary,
            @RequestParam(defaultValue = "4000.00") double maxSalary,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "0") int limit,
            @RequestParam(value = "sort", defaultValue = "id,ASC") String sortBy
    ) {
        if (minSalary < 0) {
            throw new BadInputException("Bad input - salary must be >= 0");
        }
        if (offset < 0) {
            throw new BadInputException("Bad input - offset must be >= 0");
        }
        if (limit < 0) {
            throw new BadInputException("Bad input - limit must be >= 0");
        }
        return employeeService.fetchEmployees(minSalary, maxSalary, offset, limit, sortBy);
    }

    @GetMapping("{id}")
    public Employee getEmployee(@PathVariable String id) {
        return employeeService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Object> createEmployee(@RequestBody Employee employee) {
        String message = employeeService.save(employee);
        return generateResponse(message, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateEmployee(@PathVariable String id, @RequestBody Employee employee) {
        String message = employeeService.update(id, employee);
        return generateResponse(message, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable String id) {
        String message = employeeService.delete(id);
        return generateResponse(message, HttpStatus.OK);
    }
}
