package project2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GenerateCsv {
    public static void generateCsvFile(String sFileName, ArrayList<ArrayList<String>> data){
        try {
            FileWriter writer = new FileWriter(sFileName);

            for (int i = 0; i < data.size(); i++) {
                ArrayList<String> line = data.get(i);

                for (int j = 0; j < line.size(); j++) {
                    writer.append(line.get(j));
                    writer.append(',');
                }

                writer.append('\n');
            }

            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

