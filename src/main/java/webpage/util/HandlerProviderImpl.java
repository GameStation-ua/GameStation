package webpage.util;

import com.sun.istack.NotNull;
import webpage.handlers.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class HandlerProviderImpl implements HandlerProvider {

    private final Map<HandlerType, Handler> handlers = new EnumMap<>(HandlerType.class);

    public HandlerProviderImpl(){
        final HomeHandler homeHandler = new HomeHandler();
        handlers.put(homeHandler.getType(), homeHandler);
        final LogInHandler logInHandler = new LogInHandler();
        handlers.put(logInHandler.getType(), logInHandler);
        final RegisterHandler registerHandler = new RegisterHandler();
        handlers.put(registerHandler.getType(), registerHandler);
        final SearchHandler searchHandler = new SearchHandler();
        handlers.put(searchHandler.getType(), searchHandler);
        final TagsHandler tagsHandler = new TagsHandler();
        handlers.put(tagsHandler.getType(), tagsHandler);
        final UploadHandler uploadHandler = new UploadHandler();
        handlers.put(uploadHandler.getType(), uploadHandler);
        final GameListHandler gameListHandler = new GameListHandler();
        handlers.put(gameListHandler.getType(), gameListHandler);
        final ABMGamesHandler abmGamesHandler = new ABMGamesHandler();
        handlers.put(abmGamesHandler.getType(), abmGamesHandler);
        final FollowHandler followHandler = new FollowHandler();
        handlers.put(followHandler.getType(), followHandler);
        final CommentHandler commentHandler = new CommentHandler();
        handlers.put(commentHandler.getType(), commentHandler);
        final ForumHandler forumHandler = new ForumHandler();
        handlers.put(forumHandler.getType(), forumHandler);
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

