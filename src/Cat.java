import processing.core.PApplet;
import processing.core.PImage;

public class Cat extends Creature {
    public Cat(PApplet in_pApplet, PImage img, int spawnX, int spawnY) {
        species = CAT;
        health = 5;
        image = img;
        speed = 2;
        fears = new int[] {LION, DOG};
        foods = new int[] {MOUSE, BURGER};
        pApplet = in_pApplet;
        energy = 5;
        strength = 2;

        x = spawnX;
        y = spawnY;

        pApplet.image(image, x, y);
    }
}
