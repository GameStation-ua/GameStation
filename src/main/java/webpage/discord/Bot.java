package webpage.discord;

        import discord4j.common.util.Snowflake;
        import discord4j.core.DiscordClient;
        import discord4j.core.GatewayDiscordClient;
        import webpage.model.Game;
        import webpage.requestFormats.GameUpdateRequest;

        import java.io.FileInputStream;
        import java.io.FileNotFoundException;

        import static discord4j.core.spec.MessageCreateFields.File;
        import static webpage.util.BotToken.botToken;
        import static webpage.util.ServerInitializer.imagesPath;
        import static webpage.util.ServerInitializer.frontEndLink;


public class Bot {
    // The discord bot token.
    private final static String token = botToken;

    // The ID of a webhook in a channel the bot has MANAGE_WEBHOOKS permission in.
    private final static Snowflake webhookId = Snowflake.of("997164425243021373");

    private final static GatewayDiscordClient gateway = DiscordClient.create(token)
            .gateway()
            .login()
            .block();


    public static void sendNews(Game game, GameUpdateRequest gameUpdateRequest, Long imageId){
        sendNews(game.getName() + " just posted an update!!! \n" +
                gameUpdateRequest.getTitle() + "\n" +
                "Click here for more! " + frontEndLink + gameUpdateRequest.getPath(), imageId);
    }
    private static void sendNews(String alert, Long imageId) {
        try {
            if (gateway != null) {
                gateway.getWebhookById(webhookId)
                        .flatMap(webhook -> {
                            try {
                                return webhook.execute()
                                        .withContent(alert)
                                        .withFiles(File.of("image.png", new FileInputStream(imagesPath + "/game_updates/" + imageId + ".png")));
                            } catch (FileNotFoundException e) {
                                return webhook.execute()
                                        .withContent(alert);
                            }
                        })
                        .block();
            }
        }catch (NullPointerException ignored){
        }
    }
}

