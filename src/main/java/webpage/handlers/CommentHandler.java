package webpage.handlers;

import webpage.model.Thread;
import webpage.model.*;
import webpage.requestFormats.CommentRequest;
import webpage.responseFormats.CommentListResponse;
import webpage.responseFormats.CommentResponse;
import webpage.util.HandlerType;
import webpage.util.NotificationType;

import java.util.List;
import java.util.Optional;

import static spark.Spark.*;
import static webpage.entity.Actors.*;
import static webpage.entity.Comments.createCommentResponseList;
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
            post("/game/:gameId","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                CommentRequest commentRequest = fromJson(req.body(), CommentRequest.class);
                Long userId = getIdByToken(token);
                Long gameId = Long.valueOf(req.params(":gameId"));

                Optional<Actor> game = findActorById(gameId);

                Optional<User> user = findUserById(userId);

                if (game.isEmpty() || user.isEmpty()) return returnJson(res, 401, "Something went wrong");


                Comment comment = new Comment(userId, gameId, commentRequest.getContent());
                game.get().addComment(comment);
                Notification notification = new Notification(NotificationType.FOLLOWED_USER_COMMENTS, user.get(), game.get(), commentRequest.getPath());
                try{
                    persistNotificationToFollowers(notification, user.get());
                    merge(user.get());
                    merge(game.get());
                    sendNotificationToFollowers(notification, user.get());
                    return returnJson(res, 200, "OK");
                }catch (Throwable e){
                    return returnJson(res, 500, "Something went wrong");
                }
            });

            post("/thread/:threadId","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                CommentRequest commentRequest = fromJson(req.body(), CommentRequest.class);
                Long userId = getIdByToken(token);
                Long threadId = Long.valueOf(req.params(":threadId"));

                Optional<Thread> thread = findThreadById(threadId);

                if (thread.isEmpty()) return returnJson(res, 401, "Something went wrong");

                Optional<User> creator = findUserById(thread.get().getCreatorId());
                Optional<User> user = findUserById(userId);

                if (creator.isEmpty() || user.isEmpty()) return returnJson(res, 401, "Something went wrong");
                Comment comment = new Comment(userId, threadId, commentRequest.getContent());
                thread.get().addComment(comment);
                Notification notification1 = new Notification(NotificationType.FOLLOWED_USER_COMMENTS, user.get(), thread.get(), commentRequest.getPath());
                persistNotificationToFollowers(notification1, user.get());
                Notification notification2 = new Notification(NotificationType.USER_COMMENTED_ON_USER_THREAD, user.get(), thread.get(), commentRequest.getPath());
                creator.get().addNotification(notification2);
                Notification notification3 = new Notification(NotificationType.USER_COMMENTED_ON_FOLLOWED_THREAD, user.get(), thread.get(), commentRequest.getPath());
                persistNotificationToFollowers(notification3, thread.get());
                try{
                    merge(creator.get());
                    merge(user.get());
                    merge(thread.get());
                    sendNotificationToFollowers(notification1, user.get());
                    sendNotification(creator.get().getId(), notification2);
                    sendNotificationToFollowers(notification3, thread.get());
                    return returnJson(res, 200, "OK");
                }catch (Throwable e){
                    return returnJson(res, 500, "Something went wrong");
                }
            });

            post("/user/:targetUserId","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");

                CommentRequest commentRequest = fromJson(req.body(), CommentRequest.class);

                Long userId = getIdByToken(token);
                Long targetUserId = Long.valueOf(req.params(":targetUserId"));

                Optional<User> targetUser = findUserById(targetUserId);
                Optional<User> user = findUserById(userId);
                if (user.isEmpty() || targetUser.isEmpty()) return returnJson(res, 400, "Something went wrong");
                Comment comment = new Comment(userId, targetUserId, commentRequest.getContent());
                targetUser.get().addComment(comment);
                Notification notification = new Notification(NotificationType.FOLLOWED_USER_COMMENTS, user.get(), targetUser.get(), commentRequest.getPath());
                persistNotificationToFollowers(notification, user.get());
                Notification notification1 = new Notification(NotificationType.USER_COMMENTED_ON_PROFILE, user.get(), targetUser.get(), commentRequest.getPath());
                targetUser.get().addNotification(notification1);
                try{
                    merge(user.get());
                    merge(targetUser.get());
                    sendNotificationToFollowers(notification, user.get());
                    sendNotification(targetUserId, notification1);
                    return returnJson(res, 200, "OK");
                }catch (Throwable e){
                    return returnJson(res, 500, "Something went wrong");
                }
            });

            get("/commentPage/*/*","application/json", (req, res) ->{
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                Long userId = getIdByToken(token);
                String[] request = req.splat();

                Optional<List<Comment>> comments = findCommentsFromActorById(Long.valueOf(request[0]), Integer.parseInt(request[1]));
                if (comments.isEmpty()) return returnJson(res, 400, "Something went wrong");
                List<CommentResponse> commentResponseList = createCommentResponseList(comments.get(), userId);

                res.status(200);
                return toJson(new CommentListResponse(commentResponseList));
            });

            post("/upvote/:commentId","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                Long userId = getIdByToken(token);
                Long commentId =  Long.valueOf(req.params(":commentId"));

                Optional<UserComment> userComment = findUserCommentByCommentIdAndUserId(commentId, userId);
                if (userComment.isEmpty()) return returnJson(res, 500, "Something went wrong");
                userComment.get().setVote(1);
                try {
                    merge(userComment.get());
                    return returnJson(res, 200, "OK");
                }catch (Throwable e){
                    return returnJson(res, 500, "Something went wrong");
                }
            });

            post("/downvote/:commentId","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                Long userId = getIdByToken(token);
                Long commentId =  Long.valueOf(req.params(":commentId"));

                Optional<UserComment> userComment = findUserCommentByCommentIdAndUserId(commentId, userId);
                if (userComment.isEmpty()) return returnJson(res, 500, "Something went wrong");
                userComment.get().setVote(-1);
                try {
                    merge(userComment.get());
                    return returnJson(res, 200, "OK");
                }catch (Throwable e){
                    return returnJson(res, 500, "Something went wrong");
                }
            });

            post("/neutralvote/:commentId","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                Long userId = getIdByToken(token);
                Long commentId =  Long.valueOf(req.params(":commentId"));

                Optional<UserComment> userComment = findUserCommentByCommentIdAndUserId(commentId, userId);
                if (userComment.isEmpty()) return returnJson(res, 500, "Something went wrong");
                userComment.get().setVote(0);
                try {
                    merge(userComment.get());
                    return returnJson(res, 200, "OK");
                }catch (Throwable e){
                    return returnJson(res, 500, "Something went wrong");
                }
            });
        });
    }

    @Override
    public HandlerType getType() {
        return HandlerType.COMMENT;
    }
}
