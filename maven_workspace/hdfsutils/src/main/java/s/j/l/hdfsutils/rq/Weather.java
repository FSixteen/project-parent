package s.j.l.hdfsutils.rq;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class Weather implements WritableComparable<Weather> {
  private int year;
  private int month;
  private int day;
  private int w;

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getDay() {
    return day;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public int getW() {
    return w;
  }

  public void setW(int w) {
    this.w = w;
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeInt(this.year);
    out.writeInt(this.month);
    out.writeInt(this.day);
    out.writeInt(this.w);
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    this.year = in.readInt();
    this.month = in.readInt();
    this.day = in.readInt();
    this.w = in.readInt();
  }

  @Override
  public int compareTo(Weather o) {
    int y = this.year - o.getYear();
    if (y == 0) {
      int m = this.month - o.getMonth();
      if (m == 0) {
        int w = o.getW() - this.month;
        return w;
      }
      return m;
    }
    return 0;
  }

}
