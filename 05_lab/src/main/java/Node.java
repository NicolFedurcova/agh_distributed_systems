import java.util.ArrayList;
import java.util.HashSet;

public class Node {
    private int ID;
    private int publicKey;
    private int privateKey;
    private HashSet<Integer> blockedBy = new HashSet<>();
    private HashSet<Integer> blockingWho = new HashSet<>();

    public Node(int ID, int publicKey, int privateKey) {
        this.ID = ID;
        this.publicKey = publicKey;
        this.privateKey = privateKey;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(int publicKey) {
        this.publicKey = publicKey;
    }

    public int getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(int privateKey) {
        this.privateKey = privateKey;
    }

    public HashSet<Integer> getBlockedBy() {
        return blockedBy;
    }

    public void addToBlockedBy(int blockedBy) {
        this.blockedBy.add(blockedBy);
    }

    public HashSet<Integer> getBlockingWho() {
        return blockingWho;
    }

    public void addToBlockingWho(int blockingWho) {
        this.blockingWho.add(blockingWho);
    }

    //DETECT PAHSE ******************************
    public boolean isDeadlockDetected(int incomingKey){
        //If the public and private keys of a
        //blocked process are same, and the value
        //is again same as the public key of the
        //blocking process, a deadlock is detected
        if(this.publicKey == this.privateKey && this.privateKey == incomingKey){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("DEADLOCK WAS DETECTED IN NODE "+this.ID);
            System.out.println("private key: "+this.privateKey+" public key: "+this.publicKey+" incoming key: "+incomingKey);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return true;
        }
        return false;
    }
    @Override
    public String toString() {
        return "<" + ID +
                ", publicKey=" + publicKey +
                ", privateKey=" + privateKey +
                ", blockedBy=" + blockedBy +
                ", blockingWho=" + blockingWho +
                '>';
    }
}
