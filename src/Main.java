import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.Random;

public class Main extends PApplet {

    public final int SPAWN_AREA = 1000;
    public final int MAX_BURGERS = 8;

    public int day = 1;
    public Creature selected;

    public static void main(String[] args) {
        PApplet.main(new String[] {Main.class.getName()});
    }

    @Override
    public void settings() {
        fullScreen();
        //size(WIDTH, HEIGHT);
    }

    ArrayList<Creature> allCreatures = new ArrayList<>();

    @Override
    public void setup() {

        frameRate(30);

        Random rand = new Random();

        //make dogs
        for (int i = 0; i < 4; i++) {
            int spawnX = rand.nextInt(SPAWN_AREA);
            int spawnY = rand.nextInt(SPAWN_AREA);

            allCreatures.add(new Dog(this, loadImage("dog.png"), spawnX, spawnY));
        }

        //make cats
        for (int i = 0; i < 4; i++) {
            int spawnX = rand.nextInt(SPAWN_AREA);
            int spawnY = rand.nextInt(SPAWN_AREA);

            allCreatures.add(new Cat(this, loadImage("cat.png"), spawnX, spawnY));
        }

        //make mice
        for (int i = 0; i < 5; i++) {
            int spawnX = rand.nextInt(SPAWN_AREA);
            int spawnY = rand.nextInt(SPAWN_AREA);

            allCreatures.add(new Mouse(this, loadImage("mouse.png"), spawnX, spawnY));
        }

        //make lions
        for (int i = 0; i < 2; i++) {
            int spawnX = rand.nextInt(SPAWN_AREA);
            int spawnY = rand.nextInt(SPAWN_AREA);

            allCreatures.add(new Lion(this, loadImage("lion.png"), spawnX, spawnY));
        }

        //make burgers
        spawnBurgers(10);
    }

    final int NOTICE_DISTANCE = 150;
    @Override
    public void draw() {

        //notify all creatures of all other creatures within NOTICE_DISTANCE
        notice();

        //check for creatures able to reproduce
        checkForReproduce();

        //check for creatures able to attack
        checkForAttacks();

        //check for creatures able to help
        checkForHelp();

        //remove dead creatures
        for (int i = 0; i < allCreatures.size(); i++) {
            Creature c = allCreatures.get(i);

            if (!c.alive) {
                allCreatures.remove(c);
            }
        }


        //update all creatures
        background(255);
        for (Creature c : allCreatures) {
            c.update();
        }

        final int DAY_LENGTH = 2000;
        //run once per "day"
        if (frameCount % DAY_LENGTH == 0) {
            nextDay();
            day++;
        }

        fill(0,200);
        textSize(50);
        text(("Day " + day), width-200, 50);
    }

    public void notice() {
        for (Creature creature : allCreatures) {
            ArrayList<Creature> noticed = new ArrayList<>();

            for (Creature other : allCreatures) {
                //ensure creatures do not notice themselves
                if (creature.id != other.id) {
                    if (calculateDistance(creature, other) < NOTICE_DISTANCE) {
                        noticed.add(other);
                    }
                }
            }
            creature.notice(noticed);
        }
    }

    public void checkForReproduce() {

        Creature creature;
        Creature other;

        for (int i=0; i<allCreatures.size(); i++) {
            creature = allCreatures.get(i);

            for (int j=0; j<allCreatures.size(); j++) {
                other = allCreatures.get(j);

                if (
                    //ensure creatures do not reproduce with themselves
                    (creature.id != other.id) &&

                    //creatures must be in the same location
                    (calculateDistance(creature, other) < 10) &&

                    //creatures must be the same species
                    (creature.species == other.species) &&

                    //both creatures must be sufficiently fed
                    (creature.energy > 2) && (other.energy > 2) &&

                    //creatures must not be family
                    (!creature.family.contains(other)))
                {
                    reproduce(creature, other);
                }
            }
        }
    }

    public void checkForAttacks() {

        final int ATTACK_DISTANCE = 10;

        Creature creature;
        Creature other;

        //check for creatures able to catch prey
        for (int i=0; i<allCreatures.size(); i++) {
            creature = allCreatures.get(i);

            for (int j = 0; j < allCreatures.size(); j++) {
                other = allCreatures.get(j);

                for (int food : creature.foods) {
                    if (other.species == food) {
                        if (calculateDistance(creature, other) < ATTACK_DISTANCE) {
                            attemptAttack(creature, other);
                        }

                    }
                }
            }
        }
    }

    public void attemptAttack(Creature predator, Creature prey) {

        if ((predator.priority == Creature.EAT) ||
            (predator.personality.mean) ||
            (prey.personality.aggressive))
        {
            predator.attack(prey);
        }

        if (prey.health <= 0) {
            predator.energy += prey.strength;
        }
    }

    public void checkForHelp() {

        Creature creature;
        Creature other;

        for (int i = 0; i < allCreatures.size(); i++) {
            creature = allCreatures.get(i);

            for (int j = 0; j <allCreatures.size(); j++) {
                other = allCreatures.get(j);

                if (
                    //ensure creatures do not help themselves
                    (creature.id != other.id) &&

                    //creatures must be in the same location
                    (calculateDistance(creature, other) < 10) &&

                    //creatures must be the same species
                    (creature.species == other.species) &&

                    //creature has enough energy to help
                    (creature.energy > 3) &&

                    //other creature needs help
                    (other.energy < 2) &&

                    //creatures must want to help
                    (!creature.personality.mean) &&
                    ((creature.family.contains(other)) || creature.personality.helpful))
                {
                    creature.energy -= 2;
                    other.energy += 1;
                }
            }
        }
    }

    public double calculateDistance(Creature c1, Creature c2) {
        int x1 = c1.x+(c1.image.width/2);
        int y1 = c1.y+(c1.image.height/2);
        int x2 = c2.x+(c2.image.width/2);
        int y2 = c2.y+(c2.image.height/2);

        double ac = Math.abs(y2 - y1);
        double cb = Math.abs(x2 - x1);

        return Math.hypot(ac, cb);
    }

    public double mutate(double parent1, double parent2) {
        Random rand = new Random();

        double average = (parent1 + parent2) / 2;
        double mutation;

        if ((int) average <= 0) {
            mutation = ((double) (rand.nextInt(10)-5)) / 10;
        } else {
            mutation = rand.nextInt((int) average)-average/2;
        }

        return average + mutation;
    }

    public Personality mutate(Personality parent1, Personality parent2) {
        Random rand = new Random();

        Personality child = new Personality();

        child.helpful = (new boolean[] {parent1.helpful, parent2.helpful, rand.nextBoolean()})[rand.nextInt(2)];
        child.aggressive = (new boolean[] {parent1.aggressive, parent2.aggressive, rand.nextBoolean()})[rand.nextInt(2)];
        child.mean = (new boolean[] {parent1.mean, parent2.mean, rand.nextBoolean()})[rand.nextInt(2)];
        child.friendly = (new boolean[] {parent1.friendly, parent2.friendly, rand.nextBoolean()})[rand.nextInt(2)];
        child.greedy = (new boolean[] {parent1.greedy, parent2.greedy, rand.nextBoolean()})[rand.nextInt(2)];

        return child;
    }

    public void reproduce(Creature creature1, Creature creature2) {

        int species = creature1.species;
        double health = mutate(creature1.health, creature2.health);
        PImage img = creature1.image;
        PApplet pApplet = this;
        double in_speed = mutate(creature1.speed, creature2.speed);
        double in_strength = mutate(creature1.strength, creature2.strength);
        int[] in_fears = creature1.fears;
        int[] in_foods = creature1.fears;
        Personality in_personality = mutate(creature1.personality, creature2.personality);
        int spawnX = creature1.x;
        int spawnY = creature1.y;


        Creature child = new Creature(
                        species,
                        health,
                        img,
                        pApplet,
                        in_speed,
                        in_strength,
                        in_fears,
                        in_foods,
                        in_personality,
                        spawnX,
                        spawnY
                );

        allCreatures.add(child);

        //make parents follow child
        creature1.priority = Creature.FOLLOW;
        creature1.targets.add(child);

        creature2.priority = Creature.FOLLOW;
        creature2.targets.add(child);

        //establish family
        creature1.family.add(child);
        creature2.family.add(child);

        child.family.add(creature1);
        child.family.add(creature2);

        //set parents energy levels
        creature1.energy -= 2;
        creature2.energy -= 2;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        for (Creature creature : allCreatures) {
            if ((event.getX() > creature.x) && ((creature.x + creature.image.width) > event.getX()) &&
                (event.getY() > creature.y) && ((creature.y + creature.image.height) > event.getY())) {

                //deselect last selected
                if (selected != null) {
                    selected.selected = false;
                }

                selected = creature;
                creature.selected = true;
                return;
            }
        }

        if (selected != null) {
            selected.selected = false;
            selected = null;
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKey() == 'p') {
            looping = !looping;
        }
    }


    public void nextDay() {

        int burgerCount = 0;

        for (Creature creature : allCreatures) {

            if (creature.species == Creature.BURGER) {
                burgerCount++;
            }
            //lower health if creature is exhausted
            if (creature.energy <= 0) {
                creature.health -= 2;

                //increase strength to simulate muscle growth
                creature.strength += 1;
            } else {
                //lower energy to simulate hunger
                creature.energy -= 1;

                //recover health to simulate healing
                if (creature.health < creature.maxHealth) {
                    creature.health += 1;
                }
            }
        }

        spawnBurgers(MAX_BURGERS-burgerCount);
    }

    public void spawnBurgers(int amount) {
        Random rand = new Random();
        for (int i = 0; i < amount; i++) {
            allCreatures.add(
                    new Burger(this,
                        loadImage("burger.png"),
                        rand.nextInt(SPAWN_AREA),
                        rand.nextInt(SPAWN_AREA)
                    )
            );
        }
    }
}

