package s.j.l.hdfsutils.frent;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparator;

public class FRSolr extends WritableComparator {
  public FRSolr() {
    super(Text.class, true);
  }

  @Override
  public int compare(Object a, Object b) {
    Text t1 = (Text) a;
    Text t2 = (Text) b;
    return t1.toString().compareTo(t2.toString());
  }
}
