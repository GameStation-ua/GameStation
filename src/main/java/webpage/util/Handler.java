package webpage.util;

import com.sun.istack.NotNull;

public interface Handler {

    //~ Methods ..................................................................................................................


    void handle();



    @NotNull HandlerType getType();
}
