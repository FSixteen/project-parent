package s.j.l.hdfsutils.rq;

import org.apache.hadoop.io.WritableComparator;

public class RQGroup extends WritableComparator {
  public RQGroup() {
    super(Weather.class, true);
  }

  @Override
  public int compare(Object a, Object b) {
    Weather w1 = (Weather) a;
    Weather w2 = (Weather) b;
    int c = Integer.compare(w1.getYear(), w2.getYear());
    if (c == 0) {
      c = Integer.compare(w1.getMonth(), w2.getMonth());
      return c;
    }
    return c;
  }
}
