package dev.neha.organization.analysis.model;

import com.opencsv.bean.CsvBindByName;

import java.util.Objects;

public class Employee {

    @CsvBindByName(column = "Id", required = true)
    private int id;
    @CsvBindByName(column = "firstName", required = true)
    private String firstName;
    @CsvBindByName(column = "lastName", required = true)
    private String lastName;
    @CsvBindByName(column = "salary", required = true)
    private int salary;
    @CsvBindByName(column = "managerId")
    private Integer managerId;

    public Employee(int id, String firstName, String lastName, int salary, Integer managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    public Employee() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public boolean isCeo(){
        return Objects.isNull(managerId);
    }

}
