package s.j.liu.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
  public static void main(String[] args) {
    Thing thing = new Thing(1, 1, 1, 1, 1);
  }
}

/**
 * @author LSJ
 * 
 *         物品信息
 */
class Thing {

  private double length = 0.0;
  private double width = 0.0;
  private double high = 0.0;
  private double price = 0.0;
  private double volume = 0.0;
  private int sum = 0;

  public Thing(double length, double width, double high, double price, int sum) {
    super();
    this.length = length;
    this.width = width;
    this.high = high;
    this.price = price;
    this.volume = length * width * high;
    this.sum = sum;
  }

  public Thing(double length, double width, double high, double price, double volume, int sum) {
    super();
    this.length = length;
    this.width = width;
    this.high = high;
    this.price = price;
    this.volume = volume;
    this.sum = sum;
  }

  public double getLength() {
    return length;
  }

  public void setLength(double length) {
    this.length = length;
  }

  public double getWidth() {
    return width;
  }

  public void setWidth(double width) {
    this.width = width;
  }

  public double getHigh() {
    return high;
  }

  public void setHigh(double high) {
    this.high = high;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public double getVolume() {
    return volume;
  }

  public void setVolume(double volume) {
    this.volume = volume;
  }

  public int getSum() {
    return sum;
  }

  public void setSum(int sum) {
    this.sum = sum;
  }

}

class Box {
  private double length = 0.0;
  private double width = 0.0;
  private double high = 0.0;
  private double price = 0.0;
  private double volume = 0.0;
  private int sum = 0;
  private List<Thing> things = new ArrayList<Thing>();

  public Box(double length, double width, double high, double price, int sum) {
    super();
    this.length = length;
    this.width = width;
    this.high = high;
    this.price = price;
    this.volume = length * width * high;
    this.sum = sum;
  }

  public Box(double length, double width, double high, double price, double volume, int sum) {
    super();
    this.length = length;
    this.width = width;
    this.high = high;
    this.price = price;
    this.volume = volume;
    this.sum = sum;
  }

  public double getLength() {
    return length;
  }

  public void setLength(double length) {
    this.length = length;
  }

  public double getWidth() {
    return width;
  }

  public void setWidth(double width) {
    this.width = width;
  }

  public double getHigh() {
    return high;
  }

  public void setHigh(double high) {
    this.high = high;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public double getVolume() {
    return volume;
  }

  public void setVolume(double volume) {
    this.volume = volume;
  }

  public int getSum() {
    return sum;
  }

  public void setSum(int sum) {
    this.sum = sum;
  }

  public List<Thing> getThings() {
    return things;
  }

  public void plusThing(Thing thing) {
    this.things.add(thing);
  }

  public void minusThing(int thingIndex) {
    this.things.remove(thingIndex);
  }

  public void minusThing(Thing thing) {
    this.things.remove(thing);
  }

}
