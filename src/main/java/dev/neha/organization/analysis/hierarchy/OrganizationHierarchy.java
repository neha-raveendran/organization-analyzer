package dev.neha.organization.analysis.hierarchy;

import java.util.Iterator;
import java.util.List;

/**
 * Organisation hierarchy Iterate throw each management levels
 */
public class OrganizationHierarchy implements Iterable<List<EmployeeNode>>{

    private final EmployeeNode ceoNode;

    public OrganizationHierarchy(EmployeeNode ceoNode) {
        this.ceoNode = ceoNode;
    }

    public Iterator<List<EmployeeNode>> iterator() {
        return OrgHierarchyIterator.fromCEO(ceoNode);
    }

}
