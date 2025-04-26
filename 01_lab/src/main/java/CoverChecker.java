import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CoverChecker {

    public int[][] loadMatrix(String fileName) {
        //preparation of variables
        int[][] matrix = new int[0][];
        int matrixSize = -1;

        try (Scanner reader = new Scanner(new File(fileName))) {
            int counter = 0;

            //line by line loading input file
            while (reader.hasNextLine()) {
                String row = reader.nextLine();

                //assuming the elements of matrix are separated by one space
                String[] rowArrayStr = row.split(" ");
                if (matrixSize == -1) {
                    matrixSize = rowArrayStr.length;
                    matrix = new int[matrixSize][matrixSize];
                }

                //checking if matrix is square
                if (rowArrayStr.length != matrixSize) {
                    System.err.println("Adjacency matrix needs to be of square shape.");
                }

                //loading matrix
                int[] rowArrayInt = new int[rowArrayStr.length];
                for (int i = 0; i < matrixSize; i++) {
                    rowArrayInt[i] = Integer.parseInt(rowArrayStr[i]);

                    //matrix value check
                    if (rowArrayInt[i] != 0 && rowArrayInt[i] != 1) {
                        System.out.println(rowArrayInt[i]);
                        System.err.println("Values of adjacency matrix can be only 0 or 1");
                    }
                }

                //matrix creation
                matrix[counter] = rowArrayInt;
                counter++;
            }
        } catch (FileNotFoundException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
        return matrix;
    }

    public ArrayList<Integer> checkGraphCover(int[][] matrix){
        int amountOfNodes = matrix[0].length;
        ArrayList<Integer> suitableNodes = new ArrayList<>();

        //cycle for testing all nodes as starting node
        for (int i = 0; i < amountOfNodes; i++) {
            if(checkNodeCover(matrix, i)){
                System.out.printf("Node %d is a good start node candidate\n", i);
                suitableNodes.add(i);
            } else{
                System.out.printf("Node %d is not suitable as a start node\n", i);
            }
        }
        return suitableNodes;
    }


    public Boolean checkNodeCover(int[][] matrix, int startingNode) {
        int amountOfNodes = matrix[0].length;
        HashSet<Integer> nodesCovered = new HashSet<>();
        Queue<Integer> toBeExamined = new LinkedList<>();
        nodesCovered.add(startingNode);
        toBeExamined.add(startingNode);

        while(!toBeExamined.isEmpty()){
            //pick node from queue of covered nodes waiting to be examined
            int currentNode = toBeExamined.poll();

            //check reachable neighbours of the node
            for (int j=0; j<amountOfNodes; j++) {
                if (matrix[currentNode][j] == 1 && !nodesCovered.contains(j)) {
                    nodesCovered.add(j);
                    toBeExamined.add(j);
                }
            }
            //all nodes are included in nodes covered list
            if (nodesCovered.size() == amountOfNodes) {
                return true;
            }
        }
        //to be examined s empty therefore all that could be have been examined but not all are covered
        return false;
    }

    public static void main(String[] args) {
        CoverChecker checker = new CoverChecker();
        int[][] matrix = checker.loadMatrix("01_lab/src/main/resources/matrix_input.txt");
        System.out.println("Input Adjacency Matrix:");
        for (int i = 0; i < matrix[0].length; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }
        ArrayList<Integer> suitableNodes = checker.checkGraphCover(matrix);
        System.out.println("Suitable nodes:\n"+ suitableNodes);


    }


}


