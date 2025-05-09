import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class MMDeadlockDetector {
    private HashMap<Integer, Node> network = new HashMap<Integer, Node>();
    private int clock;

    public void loadNetwork(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter input file path:");
        String path = scanner.nextLine();
        try (Scanner reader = new Scanner(new File(path))) {
            while (reader.hasNextLine()) {
                String networkSize = reader.nextLine();
            }
        }catch (FileNotFoundException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {

    }
}
