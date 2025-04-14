package dev.neha.organization.analysis.builder;

import dev.neha.organization.analysis.model.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrganizationHierarchyBuilderTest {

    @Test
    void shouldThrowErrorWhenCeoDataIsMissing() {
        var builder = new OrganizationHierarchyBuilder();
        var ex = Assertions.assertThrows(RuntimeException.class, builder::build);
        Assertions.assertEquals("Data of company CEO is missing", ex.getMessage());
    }

    @Test
    void shouldThrowErrorWhenMoreThanOneDataLikeCeo() {
        var builder = new OrganizationHierarchyBuilder();
        builder.addEmployee(new Employee(1, "CEO", "Test", 100, null));
        var ex = Assertions.assertThrows(RuntimeException.class, () ->
                builder.addEmployee(new Employee(2, "CEO", "Invalid", 1000, null)));
        Assertions.assertEquals("Invalid data in the csv: there are more than one CEO mapped please check", ex.getMessage());
    }

    @Test
    void shouldCreateOrganizationHierarchy() {
        var builder = new OrganizationHierarchyBuilder();
        builder.addEmployee(new Employee(123, "Joe", "Doe", 60000, null));
        builder.addEmployee(new Employee(125, "Bob", "Ronstad", 47000, 123));
        builder.addEmployee(new Employee(300, "Alice", "Hasacat", 50000, 124));
        builder.addEmployee(new Employee(305, "Brett", "Hardleaf", 34000, 300));
        builder.addEmployee(new Employee(124, "Martin", "Chekov", 45000, 123));

        var organizationHierarchy = builder.build();
        var iterator = organizationHierarchy.iterator();

        Assertions.assertTrue(iterator.hasNext());
        var level1 = iterator.next();
        Assertions.assertEquals(1, level1.size());
        var ceo = level1.get(0).getEmployee();
        Assertions.assertEquals(123, ceo.getId());
        Assertions.assertEquals("Joe", ceo.getFirstName());
        Assertions.assertEquals("Doe", ceo.getLastName());
        Assertions.assertEquals(60000, ceo.getSalary());
        Assertions.assertNull(ceo.getManagerId());

        Assertions.assertTrue(iterator.hasNext());
        var level2 = iterator.next();
        Assertions.assertEquals(2, level2.size());
        var l1Manager1 = level2.get(1).getEmployee();
        Assertions.assertEquals(124, l1Manager1.getId());
        Assertions.assertEquals("Martin", l1Manager1.getFirstName());
        Assertions.assertEquals("Chekov", l1Manager1.getLastName());
        Assertions.assertEquals(45000, l1Manager1.getSalary());
        Assertions.assertEquals(123, l1Manager1.getManagerId());
        var l1Manager2 = level2.get(0).getEmployee();
        Assertions.assertEquals(125, l1Manager2.getId());
        Assertions.assertEquals("Bob", l1Manager2.getFirstName());
        Assertions.assertEquals("Ronstad", l1Manager2.getLastName());
        Assertions.assertEquals(47000, l1Manager2.getSalary());
        Assertions.assertEquals(123, l1Manager2.getManagerId());

        Assertions.assertTrue(iterator.hasNext());
        var level3 = iterator.next();
        Assertions.assertEquals(1, level3.size());
        var l2Manager = level3.get(0).getEmployee();
        Assertions.assertEquals(300, l2Manager.getId());
        Assertions.assertEquals("Alice", l2Manager.getFirstName());
        Assertions.assertEquals("Hasacat", l2Manager.getLastName());
        Assertions.assertEquals(50000, l2Manager.getSalary());
        Assertions.assertEquals(124, l2Manager.getManagerId());

        Assertions.assertTrue(iterator.hasNext());
        var level4 = iterator.next();
        Assertions.assertEquals(1, level4.size());
        var l3Manager = level4.get(0).getEmployee();
        Assertions.assertEquals(305, l3Manager.getId());
        Assertions.assertEquals("Brett", l3Manager.getFirstName());
        Assertions.assertEquals("Hardleaf", l3Manager.getLastName());
        Assertions.assertEquals(34000, l3Manager.getSalary());
        Assertions.assertEquals(300, l3Manager.getManagerId());

        Assertions.assertFalse(iterator.hasNext());
    }


}