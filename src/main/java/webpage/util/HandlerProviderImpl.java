package webpage.util;
import com.sun.istack.NotNull;
import webpage.handlers.*;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class HandlerProviderImpl implements HandlerProvider {

    EntityManagerFactory emf;

    private final Map<HandlerType, Handler> handlers = new EnumMap<>(HandlerType.class);

    public HandlerProviderImpl(EntityManagerFactory emf){
        final HomeHandler homeHandler = new HomeHandler(emf);
        handlers.put(homeHandler.getType(), homeHandler);
        final LogInHandler logInHandler = new LogInHandler(emf);
        handlers.put(logInHandler.getType(), logInHandler);
        final RegisterHandler registerHandler = new RegisterHandler(emf);
        handlers.put(registerHandler.getType(), registerHandler);
        final SearchHandler searchHandler = new SearchHandler(emf);
        handlers.put(searchHandler.getType(), searchHandler);
        final TagsHandler tagsHandler = new TagsHandler(emf);
        handlers.put(tagsHandler.getType(), tagsHandler);
        final UploadHandler uploadHandler = new UploadHandler(emf);
        handlers.put(uploadHandler.getType(), uploadHandler);
        final GameListHandler gameListHandler = new GameListHandler(emf);
        handlers.put(gameListHandler.getType(), gameListHandler);
    }

    @Override
    public @NotNull Iterable<Handler> getAllHandlers(){
        return new ArrayList<>(handlers.values());
    }

    public @NotNull List<HandlerType> getAllHandlerTypes(){
        return  new ArrayList<>(handlers.keySet());
    }

    @Override
    public @NotNull Handler getHandlerForType(@NotNull HandlerType type){
        return handlers.get(type);
    }
}

