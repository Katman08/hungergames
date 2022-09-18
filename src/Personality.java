import java.util.Random;

public class Personality {
    public boolean helpful;
    public boolean aggressive;
    public boolean mean;
    public boolean friendly;
    public boolean greedy;

    public Personality() {
        Random rand = new Random();

        //chance of helping stranger of same species
        helpful = rand.nextBoolean();

        //chance of pissing off predator
        aggressive = rand.nextBoolean();

        //chance of hunting for sport
        mean = rand.nextBoolean();

        //chance of following stranger of same species
        friendly = rand.nextBoolean();

        //chance of eating beyond required amount
        greedy = rand.nextBoolean();
    }

    @Override
    public String toString() {
        return String.format("""
                Helpful to strangers: %s
                Aggressive to predators: %s
                Mean to prey: %s
                Friendly to strangers: %s
                Greedy: %s
                """,
                helpful,
                aggressive,
                mean,
                friendly,
                greedy);
    }
}
