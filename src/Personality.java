import java.util.Random;

public class Personality {
    public boolean helpful;
    public boolean aggressive;
    public boolean mean;

    public Personality() {
        Random rand = new Random();

        helpful = rand.nextBoolean();
        aggressive = rand.nextBoolean();
        mean = rand.nextBoolean();
    }
}
