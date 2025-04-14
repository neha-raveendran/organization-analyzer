package dev.neha.organization.analysis.analysers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrganizationCsvAnalyserTest {

    @Test
    void shouldGetAnalysisReport() throws IOException {
        var path = ClassLoader.getSystemResource("employees.csv").getPath();
        var analyser = new OrganizationCsvAnalyser(path);
        var report = analyser.processCsv();
        assertEquals(
                """
                         Manager 1002 (Manager,  Test) earns 1500.00 more than the maximum salary threshold. Current Salary : 1600 | Maximum salary : 100.00
                         Manager 1003 (Manager ,  Test) earns 180.00 less than the minimum salary threshold. Current Salary : 80 | Minimum salary : 260.00
                         Manager 1004 (Manager,  Test) earns 150.00 more than the maximum salary threshold. Current Salary : 400 | Maximum salary : 250.00
                         Manager 1005 (Manager ,  Test) earns 600.00 less than the minimum salary threshold. Current Salary : 400 | Minimum salary : 1000.00
                         Employee 1006 (Employee ,  Test) has a reporting line that exceeds the allowed length by 5 levels."""
                , report.stream().collect(Collectors.joining("\n")));
    }

    @Test
    void shouldBeAnEmptyAnalysisReport() throws IOException {
        var path = ClassLoader.getSystemResource("ideal_org.csv").getPath();
        var analyser = new OrganizationCsvAnalyser(path);
        var report = analyser.processCsv();
        System.out.println(report.stream().collect(Collectors.joining("\n")));
        Assertions.assertTrue(report.isEmpty());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfFileIsNotCsv() {
        var ex = Assertions.assertThrows(IllegalArgumentException.class, () -> new OrganizationCsvAnalyser("employees_not_found.txt"));
        assertEquals("'employees_not_found.txt' is not csv file", ex.getMessage());
    }

    @Test
    void shouldThrowOnProcessCsvWhenCsvMissing() {
        var analyser = new OrganizationCsvAnalyser("employees_not_found.csv");
        var ex = Assertions.assertThrows(NoSuchFileException.class, analyser::processCsv);
        assertEquals("employees_not_found.csv", ex.getFile());
    }

    @Test
    void shouldThrowOnProcessCsvWhenSalaryMissingInFile() {
        var path = ClassLoader.getSystemResource("salary_missing.csv").getPath();
        var analyser = new OrganizationCsvAnalyser(path);
        var ex = Assertions.assertThrows(RuntimeException.class, analyser::processCsv);
        assertEquals("Error parsing CSV line: 4. [125,Bob,Ronstad,,123]", ex.getMessage());
    }

    @Test
    void shouldThrowOnProcessCsvWhenFirstNameMissingInFile() {
        var path = ClassLoader.getSystemResource("first_name_missing.csv").getPath();
        var analyser = new OrganizationCsvAnalyser(path);
        var ex = Assertions.assertThrows(RuntimeException.class, analyser::processCsv);
        assertEquals("Error parsing CSV line: 3. [124,,Chekov,45000,123]", ex.getMessage());
    }

}