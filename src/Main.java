import object.GameManager;

public class Main {
    public static void main(String[] args) {

        GameManager gameManager = new GameManager();


        while(true)
        {
            gameManager.startGame();
            if(gameManager.getMoney() == 0)
                break;
        }

    }
}