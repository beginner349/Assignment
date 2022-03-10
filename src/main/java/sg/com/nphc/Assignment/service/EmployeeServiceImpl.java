package sg.com.nphc.Assignment.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sg.com.nphc.Assignment.exception.BadInputException;
import sg.com.nphc.Assignment.model.Employee;
import sg.com.nphc.Assignment.repository.CustomPageable;
import sg.com.nphc.Assignment.repository.EmployeeRepository;
import sg.com.nphc.Assignment.util.Helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public boolean save(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        AtomicBoolean changed = new AtomicBoolean(false);

        Set<String> ids = new HashSet<>();
        List<DateTimeFormatter> formatters = Helper.getStartDateFormatters();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        try (Stream<String> stream = reader.lines().skip(1)) {
            stream.forEach(line -> {
                if (!line.equals("") && !line.startsWith("#")) {
                    String[] values = line.split(Helper.COMMA_DELIMITER);
                    if (values.length != 5) {
                        throw new BadInputException("Bad input - parsing error");
                    }
                    String id = values[0];
                    if (ids.contains(id)) {
                        throw new BadInputException("Bad input - duplicate row");
                    }
                    ids.add(id);
                    String login = values[1];
                    Employee employeeWithSameLoginInDB = employeeRepository.findByLogin(login);
                    if (employeeWithSameLoginInDB != null && !employeeWithSameLoginInDB.getId().equals(id)) {
                        throw new BadInputException("Bad input - Employee login not unique");
                    }
                    String name = values[2];
                    double salary = Double.parseDouble(values[3]);
                    if (salary < 0) {
                        throw new BadInputException("Bad input - Invalid salary");
                    }
                    LocalDate startDate = null;
                    for (DateTimeFormatter formatter : formatters) {
                        try {
                            startDate = LocalDate.parse(values[4], formatter);
                            break;
                        } catch (DateTimeParseException ex) {
                        }
                    }
                    if (startDate == null) {
                        throw new BadInputException("Bad input - Invalid date");
                    }
                    Employee employeeToSave = new Employee(id, login, name, salary, startDate);
                    Optional<Employee> optional = employeeRepository.findById(id);
                    if (optional.isPresent()) {
                        Employee employeeInDB = optional.get();
                        if (!employeeInDB.equals(employeeToSave)) {
                            employeeRepository.saveAndFlush(employeeToSave);
                            changed.set(true);
                        }
                    } else {
                        employeeRepository.saveAndFlush(employeeToSave);
                        changed.set(true);
                    }
                }
            });
        }
        return changed.get();
    }

    @Override
    public List<Employee> fetchEmployees(double minSalary, double maxSalary, int offset, int limit, String sortBy) {
        String[] sortArray = sortBy.split(",");
        Sort.Direction sortDirection;
        if (sortArray[1].equalsIgnoreCase("DESC")) {
            sortDirection = Sort.Direction.DESC;
        } else {
            sortDirection = Sort.Direction.ASC;
        }
        Sort sort = Sort.by(sortDirection, sortArray[0]);
        if (limit == 0) {
            limit = Integer.MAX_VALUE;
        }
        Pageable pageable = new CustomPageable(offset, limit, sort);
        return employeeRepository.findBySalaryGreaterThanEqualAndSalaryLessThan(minSalary, maxSalary, pageable);
    }

    @Override
    public Employee findById(String id) {
        Optional<Employee> optional = employeeRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new BadInputException("Bad input - no such employee");
        }
    }

    @Override
    public String save(Employee employee) {
        if (employeeRepository.existsById(employee.getId())) {
            throw new BadInputException("Employee ID already exists");
        }
        if (employeeRepository.existsByLogin(employee.getLogin())) {
            throw new BadInputException("Employee login not unique");
        }
        if (employee.getSalary() < 0) {
            throw new BadInputException("Invalid salary");
        }
        employeeRepository.save(employee);
        return "Successfully created";
    }

    @Override
    public String update(String id, Employee employee) {
        if (!employeeRepository.existsById(id)) {
            throw new BadInputException("No such employee");
        }
        Employee employeeInDB = employeeRepository.findByLogin(employee.getLogin());
        if (employeeInDB != null && !employeeInDB.getId().equals(id)) {
            throw new BadInputException("Employee login not unique");
        }
        if (employee.getSalary() < 0) {
            throw new BadInputException("Invalid salary");
        }
        employeeRepository.save(employee);
        return "Successfully updated";
    }

    @Override
    public String delete(String id) {
        if (!employeeRepository.existsById(id)) {
            throw new BadInputException("No such employee");
        }
        employeeRepository.deleteById(id);
        return "Successfully deleted";
    }
}
