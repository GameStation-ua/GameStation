package webpage.util;

import com.sun.istack.NotNull;

import java.util.Comparator;
import java.util.List;

public interface Handler {

    //~ Methods ..................................................................................................................


    void handle();



    @NotNull HandlerType getType();
}
