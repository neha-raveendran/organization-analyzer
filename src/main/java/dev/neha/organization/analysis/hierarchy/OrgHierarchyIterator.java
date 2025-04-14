package dev.neha.organization.analysis.hierarchy;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class OrgHierarchyIterator implements Iterator<List<EmployeeNode>> {

    private List<EmployeeNode> current;

    public static OrgHierarchyIterator fromCEO(EmployeeNode ceo){
        return new OrgHierarchyIterator(List.of(ceo));
    }

    private OrgHierarchyIterator(List<EmployeeNode> nodes){
        current = nodes;
    }

    private void goNextLevel(){
        current = current.stream().map(EmployeeNode::getSubordinates)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasNext() {
        return !current.isEmpty();
    }

    @Override
    public List<EmployeeNode> next() {
        var next = current;
        goNextLevel();
        return next;
    }
}
