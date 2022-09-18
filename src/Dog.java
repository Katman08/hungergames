import processing.core.PApplet;
import processing.core.PImage;

public class Dog extends Creature {
    public Dog(PApplet in_pApplet, PImage img, int spawnX, int spawnY) {
        species = DOG;
        health = 5;
        image = img;
        speed = 2;
        fears = new int[] {LION};
        foods = new int[] {CAT, MOUSE, BURGER};
        pApplet = in_pApplet;
        energy = 5;
        strength = 4;

        x = spawnX;
        y = spawnY;

        pApplet.image(image, x, y);
    }
}
