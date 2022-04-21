package webpage.handlers;

import webpage.util.HandlerType;

import javax.persistence.EntityManagerFactory;

public class ABMGamesHandler extends AbstractHandler{
    public ABMGamesHandler(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public void handle() {

    }

    @Override
    public HandlerType getType() {
        return HandlerType.ABM_GAMES;
    }
}
