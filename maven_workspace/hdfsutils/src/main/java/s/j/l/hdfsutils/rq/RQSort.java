package s.j.l.hdfsutils.rq;

import org.apache.hadoop.io.WritableComparator;

public class RQSort extends WritableComparator {
  public RQSort() {
    super(Weather.class, true);
  }

  @Override
  public int compare(Object a, Object b) {
    Weather w1 = (Weather) a;
    Weather w2 = (Weather) b;
    int c = Integer.compare(w1.getYear(), w2.getYear());
    if (c == 0) {
      c = Integer.compare(w1.getMonth(), w2.getMonth());
      if (c == 0) {
        c = -Integer.compare(w1.getW(), w2.getW());
        return c;
      }
      return c;
    }
    return c;
  }
}
