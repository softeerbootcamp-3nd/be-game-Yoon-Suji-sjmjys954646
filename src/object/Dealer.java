package object;

import java.util.ArrayList;

public class Dealer extends Player{

    @Override
    Integer draw(Card card) {
        cardArray.add(card);
        return CardSumDealer();
    }


    public Integer CardSumDealer()
    {
        int ret = 0;
        for(int i = 0;i < cardArray.size();i++)
        {
            if(cardArray.get(i).number.equals("A"))
            {
                if(ret + 11 >= 22)
                    ret+=1;
                else
                    ret+=11;
            }
            else
            {
                ret += cardArray.get(i).value;
            }
        }
        return ret;
    }

}
