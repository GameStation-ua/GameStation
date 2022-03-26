package webpage.util;

import java.util.List;

public class LangUtils {

  private LangUtils() {}

  public static final String[] EMPTY_ARRAY = new String[0];

  public static <T> T notNull(T value, T defaultValue) {
    return value != null ? value : defaultValue;
  }

  public static String notEmpty(String value, String defaultValue) {
    return value == null || value.isEmpty() ? defaultValue : value;
  }


  public static <E> List<E> checkedList(List list) {
    //noinspection unchecked
    return (List<E>) list;
  }


}
