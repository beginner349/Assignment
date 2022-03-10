package sg.com.nphc.Assignment.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import sg.com.nphc.Assignment.convert.StartDateDeserializer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Employee {

    @Id
    private String id;
    @Column(unique = true)
    private String login;
    private String name;
    private double salary;
    @JsonDeserialize(using = StartDateDeserializer.class)
    private LocalDate startDate;

    public Employee() {
    }

    public Employee(String id, String login, String name, double salary, LocalDate startDate) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.salary = salary;
        this.startDate = startDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Double.compare(employee.salary, salary) == 0 && id.equals(employee.id) && login.equals(employee.login) && name.equals(employee.name) && startDate.equals(employee.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, name, salary, startDate);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", startDate=" + startDate +
                '}';
    }
}
