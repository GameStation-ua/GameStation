package webpage.util;

import com.sun.istack.NotNull;

public interface HandlerProvider {

    //~ Methods ..................................................................................................................

    /**
     * Return all registered Handlers.
     * @return
     */
    @NotNull
    Iterable<Handler> getAllHandlers();

    /**
     * Given the HandlerType, this method will return the corresponding {@link Handler Handler}.
     *
     * If given type is not yet implemented, an IllegalArgumentException must be thrown.
     *
     * IMPLEMENTATION NOTE: Internally all Handlers should be held with a map
     *
     * E.g.:
     * `private final Map<HandlerType, Handler> Handlers = new EnumMap<>(HandlerType.class);`
     *
     * @param type Handler's type
     * @return corresponding Handler
     */
    @NotNull
    Handler getHandlerForType(@NotNull final HandlerType type);
}