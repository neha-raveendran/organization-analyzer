package dev.neha.organization.analysis.hierarchy;

import dev.neha.organization.analysis.model.Employee;

import java.util.LinkedList;
import java.util.List;
import java.util.OptionalDouble;

public class EmployeeNode {

    private Employee employee;

    private List<EmployeeNode> subordinates = new LinkedList<>();

    public EmployeeNode() {

    }

    public EmployeeNode(Employee employee) {
        this.employee = employee;
    }

    public EmployeeNode employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public EmployeeNode addSubordinate(EmployeeNode employee){
        subordinates.add(employee);
        return this;
    }

    public void setReporter(EmployeeNode reporter) {}

    public OptionalDouble averageSalaryOfSubordinates(){
        return subordinates.stream().map(EmployeeNode::getEmployee)
                .mapToDouble(Employee::getSalary)
                .average();
    }

    public Employee getEmployee() {
        return employee;
    }

    public List<EmployeeNode> getSubordinates() {
        return subordinates;
    }

}
