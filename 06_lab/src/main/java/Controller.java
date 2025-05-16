import java.util.HashMap;


public class Controller {

    private final int[][] waitForGraph;
    private final int size;

    public Controller(int numberOfProcesses) {
        this.size = numberOfProcesses;
        this.waitForGraph = new int[numberOfProcesses][numberOfProcesses];
    }

    //method that build the waitforgraph and run cycle detection
    public void collectInformation(HashMap<Integer, Node> network) {
        //for each node and each process gets the resources  that the process is requesting
        for (Node node : network.values()) {
            for (Process process : node.getProcesses().values()) {
                //for each requested resource it gets by which process
                // is the resource held (in format processID@nodeID) and adds the edge to wait for graph
                for (String resKey : process.getRequestedResources()) {
                    String[] parts = resKey.split("@");
                    int resID = Integer.parseInt(parts[0]);
                    int resNodeID = Integer.parseInt(parts[1]);

                    Resource res = network.get(resNodeID).getResourceByID(resID);
                    String heldBy = res.getHeldBy();

                    if (heldBy != null && !heldBy.equals(process.getID() + "@" + process.getHomeNodeID())) {
                        int heldProcID = Integer.parseInt(heldBy.split("@")[0]);
                        waitForGraph[process.getID()][heldProcID] = 1;
                    }
                }
            }
        }

        //after the graph is built it runs cycle detection
        detectCycle();
    }

    //method that will start cycle detection
    public void detectCycle() {
        boolean[] visited = new boolean[size];
        boolean[] stack = new boolean[size];

        for (int i = 0; i < size; i++) {
            if (detectCycleUtil(i, visited, stack)) {
                System.out.println("!!!!!!!!!!!!!!!! Deadlock detected starting from process: " + i);
                return;
            }
        }
        System.out.println("No deadlock detected.");
    }

    //depth first search for cycles in waitForGraph
    private boolean detectCycleUtil(int node, boolean[] visited, boolean[] stack) {
        if (stack[node]) return true;
        if (visited[node]) return false;

        visited[node] = true;
        stack[node] = true;

        //if there is edge between two nodes and resursive search finds that
        // in stack at node ID index is value true cycle is detected
        for (int i = 0; i < size; i++) {
            if (waitForGraph[node][i] == 1 && detectCycleUtil(i, visited, stack)) {
                return true;
            }
        }
        stack[node] = false;
        return false;
    }
}
