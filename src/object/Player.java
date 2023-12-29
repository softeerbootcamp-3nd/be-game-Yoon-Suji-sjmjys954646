package object;

import java.util.ArrayList;

public abstract class Player {
    ArrayList<Card> cardArray;

    abstract Integer draw(Card card);

    public Player(){
        cardArray = new ArrayList<Card>();
    }

}
