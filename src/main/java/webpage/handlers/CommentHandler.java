package webpage.handlers;

import webpage.model.Thread;
import webpage.model.*;
import webpage.requestFormats.CommentRequest;
import webpage.responseFormats.CommentListResponse;
import webpage.responseFormats.CommentResponse;
import webpage.util.HandlerType;

import java.util.List;
import java.util.Optional;

import static spark.Spark.*;
import static webpage.entity.Actors.findActorById;
import static webpage.entity.Actors.findCommentsFromActorById;
import static webpage.entity.Comments.createCommentResponseList;
import static webpage.entity.Comments.findCommentById;
import static webpage.entity.Persister.merge;
import static webpage.entity.Threads.findThreadById;
import static webpage.entity.UserComments.findUserCommentByCommentIdAndUserId;
import static webpage.entity.Users.findUserById;
import static webpage.entity.Users.getIdByToken;
import static webpage.handlers.NotificationHandler.sendNotification;
import static webpage.util.Parser.fromJson;
import static webpage.util.Parser.toJson;


public class CommentHandler extends AbstractHandler{

    @Override
    public void handle() {
        path("/comment", () -> {
            post("/game/:gameId", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }
                CommentRequest commentRequest = fromJson(req.body(), CommentRequest.class);
                Long userId = getIdByToken(token);
                Long gameId = Long.valueOf(req.params(":gameId"));

                Optional<Actor> game = findActorById(gameId);

                Optional<User> user = findUserById(userId);

                if (game.isEmpty() || user.isEmpty()){
                        return returnMessage(res, 401, "Something went wrong");
                }


                Comment comment = new Comment(userId, gameId, commentRequest.getContent());
                game.get().addComment(comment);
                Notification notification = new Notification(NotificationType.FOLLOWED_USER_COMMENTS, user.get(), game.get(), commentRequest.getPath());
                try{
                    user.get().persistNotificationToFollowers(notification);
                    merge(user.get());
                    merge(game.get());
                    user.get().sendNotificationToFollowers(notification);
                    return returnMessage(res, 200, "OK");
                }catch (Throwable e){
                    return returnMessage(res, 500, "Something went wrong");
                }
            });

            post("/thread/:threadId", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }
                    CommentRequest commentRequest = fromJson(req.body(), CommentRequest.class);
                    Long userId = getIdByToken(token);
                    Long threadId = Long.valueOf(req.params(":threadId"));

                    Optional<Thread> thread = findThreadById(threadId);

                    if (thread.isEmpty()){
                        return returnMessage(res, 401, "Something went wrong");
                    }

                    Optional<User> creator = findUserById(thread.get().getCreatorId());
                    Optional<User> user = findUserById(userId);

                    if (creator.isEmpty() || user.isEmpty()){
                        return returnMessage(res, 401, "Something went wrong");
                    }
                    Comment comment = new Comment(userId, threadId, commentRequest.getContent());
                    thread.get().addComment(comment);
                    Notification notification1 = new Notification(NotificationType.FOLLOWED_USER_COMMENTS, user.get(), thread.get(), commentRequest.getPath());
                    user.get().persistNotificationToFollowers(notification1);
                    Notification notification2 = new Notification(NotificationType.USER_COMMENTED_ON_USER_THREAD, user.get(), thread.get(), commentRequest.getPath());
                    creator.get().addNotification(notification2);
                    Notification notification3 = new Notification(NotificationType.USER_COMMENTED_ON_FOLLOWED_THREAD, user.get(), thread.get(), commentRequest.getPath());
                    thread.get().persistNotificationToFollowers(notification3);
                    try{
                        merge(creator.get());
                        merge(user.get());
                        merge(thread.get());
                        user.get().sendNotificationToFollowers(notification1);
                        sendNotification(creator.get().getId(), notification2);
                        thread.get().sendNotificationToFollowers(notification3);
                        return returnMessage(res, 200, "OK");
                    }catch (Throwable e){
                        return returnMessage(res, 500, "Something went wrong");
                    }
            });

            post("/user/:targetUserId", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }

                    CommentRequest commentRequest = fromJson(req.body(), CommentRequest.class);

                    Long userId = getIdByToken(token);
                    Long targetUserId = Long.valueOf(req.params(":targetUserId"));

                    Optional<User> targetUser = findUserById(targetUserId);
                    Optional<User> user = findUserById(userId);
                    if (user.isEmpty() || targetUser.isEmpty()){
                        return returnMessage(res, 400, "Something went wrong");
                    }
                    Comment comment = new Comment(userId, targetUserId, commentRequest.getContent());
                    targetUser.get().addComment(comment);
                    Notification notification = new Notification(NotificationType.FOLLOWED_USER_COMMENTS, user.get(), targetUser.get(), commentRequest.getPath());
                    user.get().persistNotificationToFollowers(notification);
                    Notification notification1 = new Notification(NotificationType.USER_COMMENTED_ON_PROFILE, user.get(), targetUser.get(), commentRequest.getPath());
                    targetUser.get().addNotification(notification1);
                    try{
                        merge(user.get());
                        merge(targetUser.get());
                        user.get().sendNotificationToFollowers(notification);
                        sendNotification(targetUserId, notification1);
                        return returnMessage(res, 200, "OK");
                    }catch (Throwable e){
                        return returnMessage(res, 500, "Something went wrong");
                    }
            });

            get("/*/*", (req, res) ->{
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }
                    Long userId = getIdByToken(token);
                    String[] request = req.splat();

                Optional<List<Comment>> comments = findCommentsFromActorById(Long.valueOf(request[0]), Integer.parseInt(request[1]));
                if (comments.isEmpty()){
                    return returnMessage(res, 400, "Something went wrong");
                }
                List<CommentResponse> commentResponseList = createCommentResponseList(comments.get(), userId);

                return toJson(new CommentListResponse(commentResponseList));
            });

            post("/upvote/:commentId", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }
                Long userId = getIdByToken(token);
                Long commentId =  Long.valueOf(req.params(":commentId"));

                Optional<UserComment> userComment = findUserCommentByCommentIdAndUserId(commentId, userId);

                Optional<Comment> comment = findCommentById(commentId);
                if (comment.isEmpty()){
                    return returnMessage(res, 400, "Bad request");
                }
                userComment = updateCommentUpvote(userId, commentId, userComment, comment.get());
                try {
                    merge(comment.get());
                    merge(userComment.get());
                    return returnMessage(res, 200, "OK");
                }catch (Throwable e){
                    return returnMessage(res, 500, "Something went wrong");
                }
            });

            post("/downvote/:commentId", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }
                Long userId = getIdByToken(token);
                Long commentId =  Long.valueOf(req.params(":commentId"));

                Optional<UserComment> userComment = findUserCommentByCommentIdAndUserId(commentId, userId);

                Optional<Comment> comment = findCommentById(commentId);
                if (comment.isEmpty() || userComment.isEmpty()){
                    return returnMessage(res, 400, "Bad request");
                }
                userComment = updateCommentDowvote(userId, commentId, userComment, comment.get());
                try {
                    merge(comment.get());
                    merge(userComment.get());
                    return returnMessage(res, 200, "OK");
                }catch (Throwable e){
                    return returnMessage(res, 500, "Something went wrong");
                }
            });

            post("/neutralvote/:commentId", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }
                Long userId = getIdByToken(token);
                Long commentId =  Long.valueOf(req.params(":commentId"));

                Optional<UserComment> userComment = findUserCommentByCommentIdAndUserId(commentId, userId);

                Optional<Comment> comment = findCommentById(commentId);
                if (comment.isEmpty() || userComment.isEmpty()){
                    return returnMessage(res, 400, "Bad request");
                }
                userComment = updateCommentNeutral(userId, commentId, userComment, comment.get());
                try {
                    merge(comment.get());
                    merge(userComment.get());
                    return returnMessage(res, 200, "OK");
                }catch (Throwable e){
                    return returnMessage(res, 500, "Something went wrong");
                }
            });
        });
    }

    private Optional<UserComment> updateCommentUpvote(Long userId, Long commentId, Optional<UserComment> userComment, Comment comment) {
        if (userComment.isEmpty()) {
            userComment = Optional.of(new UserComment(userId, commentId, 1));
            comment.setVotes(comment.getVotes() + 1);
        }else{
            Integer votes = userComment.get().getVote();
            switch (votes){
                case 1: break;
                case 0: userComment.get().setVote(1); comment.setVotes(comment.getVotes() + 1); break;
                case -1: userComment.get().setVote(1); comment.setVotes(comment.getVotes() + 2); break;
            }
        }
        return userComment;
    }

    private Optional<UserComment> updateCommentDowvote(Long userId, Long commentId, Optional<UserComment> userComment, Comment comment) {
        if (userComment.isEmpty()) {
            userComment = Optional.of(new UserComment(userId, commentId, 1));
            comment.setVotes(comment.getVotes() + 1);
        }else{
            Integer votes = userComment.get().getVote();
            switch (votes){
                case 1: userComment.get().setVote(-1); comment.setVotes(comment.getVotes() - 2);break;
                case 0: userComment.get().setVote(-1); comment.setVotes(comment.getVotes() - 1); break;
                case -1: break;
            }
        }
        return userComment;
    }

    private Optional<UserComment> updateCommentNeutral(Long userId, Long commentId, Optional<UserComment> userComment, Comment comment) {
        if (userComment.isEmpty()) {
            userComment = Optional.of(new UserComment(userId, commentId, 1));
            comment.setVotes(comment.getVotes() + 1);
        }else{
            Integer votes = userComment.get().getVote();
            switch (votes){
                case 1: userComment.get().setVote(0); comment.setVotes(comment.getVotes() - 1); break;
                case 0: break;
                case -1: userComment.get().setVote(0); comment.setVotes(comment.getVotes() + 1); break;
            }
        }
        return userComment;
    }

    @Override
    public HandlerType getType() {
        return HandlerType.COMMENT;
    }
}
