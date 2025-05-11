import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class MMDeadlockDetector {
    private final HashMap<Integer, Node> network = new HashMap<Integer, Node>();
    private int networkSize;
    private final ArrayList<int[]> blocksWaiting = new ArrayList<>();
    private int keysCounter = 2;

    //method for displaying state of the network
    public void printStateOfNetwork() {
        System.out.println("-----------------------STATE OF THE NETWORK:");
        for (Node node : this.network.values()) {
            System.out.println(node);
        }
    }

    //this method executes block phase
    public void block(int[] block) {
        //BLOCK PHASE *****************************************&
        Node blocked = network.get(block[0]);
        Node blocker = network.get(block[1]);

        System.out.println("PROCESSING BLOCK: " + Arrays.toString(block));

        //adding the block edge if such edge is not added yet
        if (!blocked.getBlockedBy().contains(blocker.getID())
                &&
                !blocker.getBlockingWho().contains(blocked.getID())) {

            blocked.addToBlockedBy(blocker.getID());
            blocker.addToBlockingWho(blocked.getID());

            //creating and setting new (UNIQUE) keys in blocked process
            // that are greater than public keys of both blocked and blocker process
            int newKey = Math.max(blocked.getPublicKey(), blocker.getPublicKey()) * this.keysCounter;
            blocked.setPrivateKey(newKey);
            blocked.setPublicKey(newKey);
            this.keysCounter++;
        }
    }

    //this method propagates the public key of the blocking process to the blocked processes (recursively)
    public boolean propagateGreaterKey(int from, int propagateKey, HashSet<Integer> toWhom) {
        System.out.println("Transmitting public key from " + from + " to " + toWhom);
        boolean isDeadlock = false;
        for (int blockedNodeID : toWhom) {
            Node blocked = this.network.get(blockedNodeID);
            //DETECT PHASE **************************
            isDeadlock = blocked.isDeadlockDetected(propagateKey);
            if (isDeadlock) {
                return true;
            }
            //if deadlock was not detected the key of blocking process is propagated further
            if (propagateKey > blocked.getPublicKey()) {
                blocked.setPublicKey(propagateKey);
                return propagateGreaterKey(blocked.getID(), propagateKey, blocked.getBlockingWho());
            }
        }
        return isDeadlock;
    }

    //this method execute the transmit phase of the MMDDD algorithm
    public boolean transmit(int[] block) {
        boolean isDeadlock = false;
        Node blocked = network.get(block[0]);
        Node blocker = network.get(block[1]);

        //TRANSMIT PHASE ********************************
        if (blocker.getPublicKey() > blocked.getPublicKey()) {
            //start of the propagation of the public key of blocking process
            isDeadlock = this.propagateGreaterKey(blocker.getID(), blocker.getPublicKey(), blocker.getBlockingWho());
            if (isDeadlock) {
                return isDeadlock;
            }
        }
        return isDeadlock;
    }

    //this method initiates the network based on the input file
    public void initiate(Scanner reader) {
        //INITIATE PHASE ***************************************
        this.networkSize = Integer.parseInt(reader.nextLine());
        for (int i = 0; i < this.networkSize; i++) {
            Node node = new Node(i, i, i);
            network.put(i, node);
        }
    }

    //method for loading the blocks from input file
    public void readBlocks(Scanner reader) {
        //loading blocks
        while (reader.hasNextLine()) {
            String[] blocksStr = reader.nextLine().split(",");
            int[] block = new int[blocksStr.length];
            //blocked
            block[0] = Integer.parseInt(blocksStr[0].trim());
            //blocker
            block[1] = Integer.parseInt(blocksStr[1].trim());
            blocksWaiting.add(block);
        }
    }

    //method for loading the input - network and blocks
    public void loadInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter input file path:");
        String path = scanner.nextLine();
        try (Scanner reader = new Scanner(new File(path))) {
            //call INITIATE PHASE
            this.initiate(reader);
            //READING BLOCKS
            this.readBlocks(reader);

        } catch (FileNotFoundException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    //this method starts simulation of the network
    public void startSimulation() {
        for (int i = 0; i < this.blocksWaiting.size(); i++) {
            int[] blockIDs = this.blocksWaiting.get(i);
            //call BLOCK PHASE
            block(blockIDs);
            //call TRANSMIT PHASE, within which there is DETECT PHASE
            boolean isDeadlock = transmit(blockIDs);
            if (isDeadlock) return;
            printStateOfNetwork();
        }
    }

    public static void main(String[] args) { //05_lab/src/main/resources/input1.txt
        MMDeadlockDetector deadlockDetector = new MMDeadlockDetector();
        //call INITIATE PHASE
        deadlockDetector.loadInput();
        deadlockDetector.printStateOfNetwork();
        //we run two rounds of the simulation,
        // because the deadlock might not be detected in teh first one
        for (int i = 0; i < 2; i++) {
            deadlockDetector.startSimulation();
        }

    }
}
