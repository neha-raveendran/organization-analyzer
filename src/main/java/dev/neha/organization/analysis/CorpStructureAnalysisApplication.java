package dev.neha.organization.analysis;

import dev.neha.organization.analysis.analysers.OrganizationCsvAnalyser;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Optional;

public class CorpStructureAnalysisApplication {

    public static void main(String[] args) {
        try {
            Optional.ofNullable(args)
                    .filter(a -> a.length > 0)
                    .map(a -> a[0])
                    .map(CorpStructureAnalysisApplication::processCsv)
                    .ifPresentOrElse(report -> report.forEach(System.out::println), () ->
                            System.out.println("""
                                    employee-analysis: CSV file path is missing
                                    usage: employee-analysis [CSV-File-Path]
                                    """)
                    );
        } catch (RuntimeException e) {
            System.out.println(String.format(
                    """
                            employee-analysis: %s
                            usage: employee-analysis [CSV-File-Path]
                            """,
                    e.getMessage()
            ));
        }
    }

    private static List<String> processCsv(String csvPath) {
        try {
            return new OrganizationCsvAnalyser(csvPath).processCsv();
        } catch (IOException e) {
            if (e instanceof NoSuchFileException ex) {
                throw new RuntimeException(ex.getFile() + " CSV File not found");
            }
            if (e instanceof AccessDeniedException ex) {
                throw new RuntimeException(ex.getFile() + " CSV File access denied");
            }
            throw new RuntimeException(e);
        }
    }
}