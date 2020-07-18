package org.minibus.aqa.core.helpers;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvHelper {

    private static final char SEPARATOR = ',';

    private static CSVReader getDefaultReader(String file, char separator) {
        try {
            FileReader filereader = new FileReader(file);
            return new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .withCSVParser(new CSVParserBuilder().withSeparator(separator).build())
                    .build();
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static List<String> read(String file, int column) {
        List<String> columnData = new ArrayList<>();

        try {
            List<String[]> allData = getDefaultReader(file, SEPARATOR).readAll();

            for (String[] row : allData) {
                columnData.add(row[column - 1]);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return columnData;
    }
}
