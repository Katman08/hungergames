import processing.core.PApplet;
import processing.core.PImage;

public class Lion extends Creature {
    public Lion(PApplet in_pApplet, PImage img, int spawnX, int spawnY) {
        species = LION;
        health = 5;
        image = img;
        speed = 1;
        fears = new int[] {};
        foods = new int[] {MOUSE, CAT, DOG};
        pApplet = in_pApplet;
        energy = 5;
        strength = 8;

        x = spawnX;
        y = spawnY;

        pApplet.image(image, x, y);
    }
}
