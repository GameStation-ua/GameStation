package webpage.handlers;

import webpage.util.HandlerType;

import javax.persistence.EntityManagerFactory;

public class ABMGamesHandler extends AbstractHandler{
    public ABMGamesHandler(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public void handle() {
// todo implementar ABM juegos
    }

    @Override
    public HandlerType getType() {
        return HandlerType.ABM_GAMES;
    }
}
