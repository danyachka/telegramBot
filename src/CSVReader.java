import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class CSVReader {

    private static CSVReader Instance;

    private final String path;

    private HashMap<String, String> answers = new HashMap<>();

    public CSVReader() {
        Path currentRelativePath = Paths.get("").toAbsolutePath();
        path = currentRelativePath.toString() + "\\table.csv";

        System.out.println("table path is " + path);

        try {
            loadAnswers();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Скорее всего не найден файл");
        }

        Instance = this;
    }

    private void loadAnswers() throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader(path));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(";");
            try {
                answers.put(data[0], data[1]);
            } catch (Exception e) {
                System.out.println("В таблице не полная строка");
            }
        }
        csvReader.close();
    }

    public String getText(String key) {
        String answer = answers.get(key);

        if (answer == null) {
            answer = "no text";
        }

        return answer;
    }

    public static CSVReader getInstance() {
        return Instance;
    }

}
