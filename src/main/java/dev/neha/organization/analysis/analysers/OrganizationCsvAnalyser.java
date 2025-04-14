package dev.neha.organization.analysis.analysers;

import dev.neha.organization.analysis.builder.OrganizationHierarchyBuilder;
import dev.neha.organization.analysis.model.Employee;
import com.opencsv.bean.CsvToBeanBuilder;
import dev.neha.organization.analysis.hierarchy.EmployeeNode;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class OrganizationCsvAnalyser {

    private final int minSalaryPercent = 20;
    private final int maxSalaryPercent = 50;
    private final int maxLevel = 4;

    private final Path csvPath;

    public OrganizationCsvAnalyser(String csvPath) {
        if (!csvPath.endsWith(".csv")) {
            throw new IllegalArgumentException("'" + csvPath + "' is not csv file");
        }
        this.csvPath = Path.of(csvPath);
    }

    /**
     * Process csv and create the report
     *
     * @return report string
     */
    public List<String> processCsv() throws IOException {
        try (Reader reader = Files.newBufferedReader(csvPath)) {
            var cb = new CsvToBeanBuilder<Employee>(reader)
                    .withType(Employee.class)
                    .build();
            var organizationHierarchyBuilder = new OrganizationHierarchyBuilder();
            cb.stream().forEach(organizationHierarchyBuilder::addEmployee);
            var orgHierarchy = organizationHierarchyBuilder.build();
            // Level calculating from ceo
            var level = 0;
            var report = new LinkedList<String>();
            for (var orgLevel : orgHierarchy) {
                System.out.println(String.format("Analysing Level %d managers.... \n", level));
                for (var empNode : orgLevel) {
                    validateSalary(empNode)
                            .ifPresent(report::add);
                    if (level > maxLevel) {
                        report.add(String.format("Employee %d (%s , %s) has a reporting line that exceeds the allowed length by %d levels. \n",
                                        empNode.getEmployee().getId(), empNode.getEmployee().getFirstName(), empNode.getEmployee().getLastName(),
                                        level
                                ));
                    }
                }
                level++;
            }
            return report;
        }
    }

    private double minSalary(double avg) {
        return avg + avg * (minSalaryPercent / 100);
    }

    private double maxSalary(double avg) {
        return avg + avg * (maxSalaryPercent / 100);
    }

    /**
     * Validate the salary and return the message
     *
     * @param employeeNode employee node
     * @return validated message if any;
     */
    private Optional<String> validateSalary(EmployeeNode employeeNode) {
        var employee = employeeNode.getEmployee();
        // Skipping salary validation for ceo
        if (employee.isCeo()) {
            return Optional.empty();
        }
        var avgSalary = employeeNode.averageSalaryOfSubordinates();
        if (avgSalary.isEmpty()) {
            return Optional.empty();
        }
        var avg = avgSalary.getAsDouble();
        var minSalary = minSalary(avg);
        var maxSalary = maxSalary(avg);
        if (employee.getSalary() < minSalary) {
            return Optional.of(String.format("Manager %d (%s , %s) earns %.2f less than the minimum salary threshold. \n Current Salary : %d | Minimum salary : %.2f \n",
                    employee.getId(), employee.getFirstName(), employee.getLastName(),
                    minSalary - employee.getSalary(),
                    employee.getSalary(),
                    minSalary
            ));
        }
        if (employee.getSalary() > maxSalary) {
            return Optional.of(String.format(
                    "Manager %d (%s, %s) earns %.2f more than the maximum salary threshold. \n  Current Salary : %d | Maximum salary : %.2f \n",
                    employee.getId(), employee.getFirstName(), employee.getLastName(),
                    employee.getSalary() - maxSalary,
                    employee.getSalary(),
                    maxSalary
            ));
        }
        return Optional.empty();
    }

}
