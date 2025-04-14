package dev.neha.organization.analysis;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class OrganizationAnalyzerApplicationTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(standardOut);
    }

    @Test
    public void shouldSystemOutArgMissing(){
        OrganizationAnalyzerApplication.main(null);
        Assertions.assertEquals("""
                employee-analysis: CSV file path is missing
                usage: employee-analysis [CSV-File-Path]""", outputStreamCaptor.toString()
                .trim());
    }

    @Test
    public void shouldSystemOutCsvArgMissing(){
        OrganizationAnalyzerApplication.main(new String[]{});
        Assertions.assertEquals("""
                employee-analysis: CSV file path is missing
                usage: employee-analysis [CSV-File-Path]""", outputStreamCaptor.toString()
                .trim());
    }

    @Test
    public void shouldSystemOutCSVFileNotFound(){
        OrganizationAnalyzerApplication.main(new String[]{"emp.csv"});
        Assertions.assertEquals("""
                employee-analysis: emp.csv CSV File not found
                usage: employee-analysis [CSV-File-Path]""", outputStreamCaptor.toString()
                .trim());
    }

    @Test
    public void shouldSystemOutInvalidCSV(){
        var path = ClassLoader.getSystemResource("salary_missing.csv").getPath();
        OrganizationAnalyzerApplication.main(new String[]{path});
        Assertions.assertEquals("""
                employee-analysis: Error parsing CSV line: 4. [125,Bob,Ronstad,,123]
                usage: employee-analysis [CSV-File-Path]""", outputStreamCaptor.toString()
                .trim());
    }

}