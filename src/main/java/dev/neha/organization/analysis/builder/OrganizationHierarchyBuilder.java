package dev.neha.organization.analysis.builder;

import dev.neha.organization.analysis.model.Employee;
import dev.neha.organization.analysis.hierarchy.EmployeeNode;
import dev.neha.organization.analysis.hierarchy.OrganizationHierarchy;
import org.apache.commons.lang3.builder.Builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Organisation hierarchy builder for creating Organisation hierarchy object
 */
public class OrganizationHierarchyBuilder implements Builder<OrganizationHierarchy> {

    private final Map<Integer, EmployeeNode> hierarchyMap = new HashMap<>();
    private EmployeeNode ceoNode;

    public OrganizationHierarchyBuilder() {

    }

    public void addEmployee(Employee employee) {
        var employeeNode = Optional.ofNullable(hierarchyMap.get(employee.getId()))
                .map(node -> node.employee(employee))
                .orElseGet(() -> createEmployeeNode(employee));
        if (Objects.isNull(employee.getManagerId())) {
            if (Objects.nonNull(ceoNode)) {
                throw new IllegalArgumentException("Invalid data in the csv: there are more than one CEO mapped please check");
            }
            ceoNode = employeeNode;
            return;
        }
        var manager = Optional.of(employee.getManagerId())
                .map(managerId -> hierarchyMap.get(managerId))
                .orElseGet(() -> createManagerNode(employee.getManagerId()));
        manager.addSubordinate(employeeNode);
        employeeNode.setReporter(manager);
    }

    private EmployeeNode createManagerNode(int managerId) {
        var node = new EmployeeNode();
        hierarchyMap.put(managerId, node);
        return node;
    }

    private EmployeeNode createEmployeeNode(Employee employee) {
        var node = new EmployeeNode(employee);
        hierarchyMap.put(employee.getId(), node);
        return node;
    }

    public OrganizationHierarchy build() {
        if (Objects.isNull(ceoNode)) {
            throw new RuntimeException("Data of company CEO is missing");
        }
        return new OrganizationHierarchy(ceoNode);
    }

}
