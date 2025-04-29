import java.util.ArrayList;
import java.util.Arrays;

public class Node {
    //based on Lampold Clock model
    public int timeStampCounter = 0;
    public int ID;
    public ArrayList<Integer> listOfRequests = new ArrayList<>();
    public boolean[] goAheadResponses;

    public Node(int ID, int sizeOfNetwork) {
        this.ID = ID;
        this.goAheadResponses = new boolean[sizeOfNetwork];
    }

    public Node(int ID, int timeStampCounter,int sizeOfNetwork) {
        this.ID = ID;
        this.timeStampCounter = timeStampCounter;
        this.goAheadResponses = new boolean[sizeOfNetwork];
    }

    public Node(int ID, int timeStampCounter, ArrayList<Integer> listOfRequests,int sizeOfNetwork) {
        this.ID = ID;
        this.timeStampCounter = timeStampCounter;
        this.listOfRequests = listOfRequests;
        this.goAheadResponses = new boolean[sizeOfNetwork];
    }

    public Node(int ID, int timeStampCounter, ArrayList<Integer> listOfRequests, boolean[] goAheadResponses,int sizeOfNetwork) {
        this.ID = ID;
        this.timeStampCounter = timeStampCounter;
        this.listOfRequests = listOfRequests;
        this.goAheadResponses = goAheadResponses;
    }

    @Override
    public String toString() {
        return "{" +"<"+
                timeStampCounter +", "+
                ID +">, "+
                listOfRequests +", "+
                Arrays.toString(goAheadResponses) +
                "}";
    }
}

