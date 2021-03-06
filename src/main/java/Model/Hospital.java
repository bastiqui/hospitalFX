package Model;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Hospital {
    private Map<String, Pacient> map_pacients = new HashMap<>();

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Collection<Pacient> loadPacients(File file) {
        boolean carrega = checkCSV(file);
        if (carrega) {
            return map_pacients.values();
        } else return null;
    }

    public boolean checkCSV (File file) {
        CSVReader csvreader = null;
        String[] line;
        try {
            csvreader = new CSVReader(new FileReader(file));
            csvreader.readNext(); //saltem primera línia de titols
            while ((line = csvreader.readNext()) != null) {
                map_pacients.putIfAbsent(line[0],
                        new Pacient(line[0],
                                line[1],
                                line[2],
                                LocalDate.parse(line[3],formatter),
                                Persona.Genere.valueOf(line[4]),
                                line[5],
                                Float.valueOf(line[6]),
                                Integer.valueOf(line[7]))
                );
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}