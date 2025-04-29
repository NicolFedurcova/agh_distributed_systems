import java.util.Arrays;
import java.util.HashMap;

public class Network {

    private final HashMap<Integer, Node> nodes= new HashMap<>();
    public int numberOfNodes;

    public Network(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

    public void addNode(Node node){
        this.nodes.put(node.ID, node);
    }

    public Node getNode(int ID){
        return this.nodes.get(ID);
    }

    public HashMap<Integer, Node> getNetworkNodes(){
        return this.nodes;
    }

    public void soutStatusOfNetwork(){
        System.out.println("STATE OF THE NETWORK");
        System.out.println("GO AHEAD RESPONSES:");
        for (int key : this.nodes.keySet()) {
            Node current = nodes.get(key);
            System.out.println("NODE " + current.ID + " : " + Arrays.toString(current.goAheadResponses));
        }
        System.out.println("LISTS OF REQUESTS");
        for (int key : this.nodes.keySet()) {
            Node current = nodes.get(key);
            System.out.println("NODE " + current.ID + " : " + current.listOfRequests);
        }
    }


}
