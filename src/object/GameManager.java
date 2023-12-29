package object;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GameManager {
    User user;
    Dealer dealer;
    Integer money;
    ArrayList<Card> deck;
    Integer deckIndex;
    Integer round;
    Integer curBettingmMoney;

    public Integer getMoney()
    {
        return money;
    }


    public GameManager()
    {
        user = new User();
        dealer = new Dealer();
        money = 100;
        deck = new ArrayList<Card>();
        deckIndex = 0;
        round = 1;
        cardInitialize();
    }

    void cardInitialize()
    {
        String[] shapes = {"♣", "♠", "♥", "♦"};
        for (int i = 2;i < 10;i++)
        {
            for(int j = 0;j < 4;j++)
            {
                Card card = new Card(shapes[j], Integer.toString(i), i);
                deck.add(card);
            }
        }

        String[] notNumber = {"A", "J", "Q", "K"};
        for (int i = 0;i < 4;i++)
        {
            for(int j = 0;j < 4;j++)
            {
                int x = (i == 0) ? 1 : 0;
                Card card = new Card(shapes[j], notNumber[i], 10 + x);
                deck.add(card);
            }
        }

    }

    void cardShuffle()
    {
        Collections.shuffle(deck);
        cardhand(user);
        cardhand(dealer);
    }

    void cardhand(Player player)
    {
        for(int i = 0;i < 2;i++)
        {
            player.cardArray.add(deck.get(deckIndex));
            //player.cardSum+=deck.get(deckIndex).value;
            deckIndex++;
        }

    }


    public String CardPrint(Player player, Integer index)
    {
        return player.cardArray.get(index).shape + player.cardArray.get(index).number;
    }

    void showCard()
    {
        System.out.print("Dealer : XX ");
        for(int i = 1;i < dealer.cardArray.size();i++)
            System.out.print(CardPrint(dealer, i) + " ");
        System.out.println();
        System.out.print("User : ");
        for(int i = 0;i < user.cardArray.size();i++)
            System.out.print(CardPrint(user, i) + " ");
        System.out.println();
    }

    void debug_CardStatus()
    {
        for (int i = 0;i < deck.size();i++)
        {
            System.out.println(deck.get(i).shape + deck.get(i).number + " " + Integer.toString(deck.get(i).value));
        }
    }

    void debug_DealerPlayerStatus()
    {
        for(int i = 0;i < dealer.cardArray.size();i++)
            System.out.println(dealer.cardArray.get(i).shape + dealer.cardArray.get(i).number + " " + Integer.toString(dealer.cardArray.get(i).value));

        for(int i = 0;i < user.cardArray.size();i++)
            System.out.println(user.cardArray.get(i).shape + user.cardArray.get(i).number + " " + Integer.toString(user.cardArray.get(i).value));

    }


    public void showRound(){
        System.out.println("현재 라운드는 : " + round + " 입니다.");
    }
    public void showMoney(){
        System.out.println("현재 가지고있는 " + money + "이하로 배팅해주세요.");
    }

    public void getBettingMoney(){
        Scanner scanner = new Scanner(System.in);

        while(true)
        {
            try {
                curBettingmMoney = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                continue;
            }

            if(curBettingmMoney <= money)
                break;

            System.out.println("다시 입력해주세요.");
        }

        System.out.println("이번 라운드 베팅 금액은 " + curBettingmMoney + "입니다.");
    }

    public Boolean userAsk(){
        System.out.println("받으시겠습니까? Y/ N");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();
        System.out.println(answer);
        if (answer.equals("Y"))
            return true;
        else
            return false;
    }

    public void startGame() {
        showRound();
        showMoney();
        getBettingMoney();
        cardShuffle();

        Boolean DealerBurst = false;
        Boolean DealerSeventeen = false;
        Boolean Win = false;
        Boolean Lose = false;
        Boolean BlackJack = false;
        while(true)
        {

            showCard();
            Integer dealerSum = 0;
            if(!DealerBurst && !DealerSeventeen)
            {
                dealerSum = dealer.draw(deck.get(deckIndex));
                deckIndex++;

                System.out.println(dealerSum);
            }


            if(dealerSum >= 22)
                DealerBurst = true;
            else if(dealerSum >= 17)
                DealerSeventeen = true;

            if(DealerBurst)
            {
                Win = true;
                break;
            }

            if(userAsk())
            {
                System.out.println("userAsk yes");
                Integer userSum = user.draw(deck.get(deckIndex));
                deckIndex++;
            }
            else
            {
                System.out.print("userAsk No");
                Integer userSum = 0;
                for(int i = 0;i < user.cardArray.size();i++)
                {
                    if(user.cardArray.get(i).number.equals("A"))
                    {
                        System.out.println(Integer.toString(i+1) + "번째 A카드를 1로 쓰시겠습니까?");
                        Scanner scanner = new Scanner(System.in);
                        String answer = scanner.next();

                        if(answer.equals("Y"))
                            userSum += 1;
                        else
                            userSum += 11;
                    }
                    else
                        userSum += user.cardArray.get(i).value;
                }

                if(userSum >= 22)
                {
                    System.out.println(userSum + " BURST!!");
                    Lose = true;
                    break;
                }

                if(userSum > dealerSum)
                {
                    if(userSum == 21)
                        BlackJack = true;
                    else
                        Win=true;
                }
                else if(userSum <= dealerSum)
                {
                    Lose= true;
                }
            }
        }

        if(Win)
        {
            money += curBettingmMoney;
            System.out.println("승리하였습니다. " + money + "가지고 있습니다.");
        }
        else if(BlackJack)
        {
            money += curBettingmMoney * 2;
            System.out.println("블랙잭입니다. " + money + "가지고 있습니다.");
        }
        else if(Lose)
        {
            money -= curBettingmMoney;
            System.out.println("패배하였습니다. " + money + "가지고 있습니다.");

        }

        if(money < 0)
            money = 0;
    }
}
