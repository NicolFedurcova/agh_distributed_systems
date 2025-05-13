import java.util.HashMap;

public class Controller {

    private int[][] waitForGraph;

    public Controller(int numberOfProcesses) {
        this.waitForGraph = new int[numberOfProcesses][numberOfProcesses];
    }

    //fills waitforGraph with oriented edges
    public void collectInformation(HashMap<Integer, Node> network){
        for(Node node : network.values()){
            for(Process process: node.getProcesses().values()){
                int givenResource = process.getGivenResourceIDAndHomeNodeID()[0];
                int requestingResource = process.getRequestingResourceIDAndHomeNodeID()[0];

            }
        }
    }

    //searches for cycles in wait-for-Graph
    public boolean isDeadlock(){
        return false;
    }
}
