package SB.model;

import java.util.ArrayList;
import java.util.Stack;

public class Player {
    public static final int FROM_MAIN_CARDS = 1;
    public static final int FROM_STORAGE_CARDS = 2;
    public static final int FROM_CURRENT_CARDS = 3;
    public static final int MOVE = 10;
    public static final int MOVE_END = 11;

    protected ArrayList<Integer> mainCards;
    protected ArrayList<Stack<Integer>> storage;
    protected ArrayList<Integer> currentCards;
    protected int id;
    public Player(int id) {
        this.id = id;

        mainCards = new ArrayList<>();
        storage = new ArrayList<>();
        currentCards = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Stack<Integer> temp = new Stack<>();
            temp.add(0);
            storage.add(temp);
        }
    }

    public Player(String string) {
        String[] parameters = string.split("/");
        id = Integer.parseInt(parameters[0]);

        mainCards = new ArrayList<>();
        storage = new ArrayList<>(4);
        currentCards = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Stack<Integer> temp = new Stack<>();
            storage.add(temp);
        }

        String[] mainC = parameters[1].split(" ");
        mainCards = new ArrayList<>(mainC.length);
        for (int i = 0; i < mainC.length; i++) {
            mainCards.add(Integer.parseInt(mainC[i]));
        }

        String[] storageC = parameters[2].split("%");
        for (int i = 0; i < 4; i++) {
            String[] cards = storageC[i].split(" ");
            Stack<Integer> temp = new Stack<>();
            for (int j = 0; j < cards.length; j++) {
                temp.add(Integer.parseInt(cards[j]));
            }
            storage.set(i, temp);
        }
        if(parameters.length > 3) {
            String[] currentC = parameters[3].split(" ");
            currentCards = new ArrayList<>(currentC.length);
            for (int i = 0; i < currentC.length; i++) {
                currentCards.add(Integer.parseInt(currentC[i]));
            }
        }
    }

    public String allInfo() {
        String result = id + "/";
        for (int i = 0; i < mainCards.size(); i++) {
            if(i == mainCards.size() - 1) {
                result += mainCards.get(i) + "/";
            }
            else result += mainCards.get(i) + " ";
        }
        for (int i = 0; i < 4; i++) {
            Stack<Integer> temp = storage.get(i);
            for (int j = 0; j < temp.size(); j++) {
                if(j == temp.size() - 1 && i == 3) {    //////////////////////////мб ошибка
                    result += temp.get(j) + "/";
                }
                else if(j == temp.size() - 1) {
                    result += temp.get(j) + "%";
                }
                else result += temp.get(j) + " ";
            }
        }
        for (int i = 0; i < currentCards.size(); i++) {
            if(i == currentCards.size() - 1) {
                result += currentCards.get(i);
            }
            else {
                result += currentCards.get(i) + " ";
            }
        }
        return result;
    }

    public void generateCards(int cardsCount) {
        for (int i = 0; i < cardsCount; i++) {
            mainCards.add((int)(Math.random()*13 + 1));
        }

        for (int i = 0; i < 4; i++) {
            Stack<Integer> temp = new Stack<>();
            temp.add(0);
            storage.add(temp);
        }

        for (int i = 0; i < 5; i++) {
            currentCards.add((int)(Math.random()*13 + 1));
        }
    }

    public void addCurrentCards() {
        for (int i = currentCards.size(); i < 5; i++) {
            currentCards.add((int)(Math.random()*13 + 1));
        }
    }

    public boolean makeMove(String code, int usedFor, Game game) {
        boolean isMade = false;
        if(usedFor == MOVE) {
            int from = code.charAt(0) - '0';
            int line = code.charAt(1) - '0';
            int to = code.charAt(2) - '0';
            if (from == FROM_MAIN_CARDS) {
                if (to >= 0 && to < 4) {
                    if(!mainCards.isEmpty()) {
                        if (mainCards.get(0) == game.getBoard().getValue(to) + 1 || mainCards.get(0) == 13) {
                            if(mainCards.get(0) == game.getBoard().getValue(to) + 1) {
                                game.getBoard().setIsSkipBoCard(false, to);
                            }
                            else if(mainCards.get(0) == 13) {
                                game.getBoard().setIsSkipBoCard(true, to);
                            }
                            game.getBoard().setValue(to, game.getBoard().getValue(to) + 1);
                            mainCards.remove(0);
                            isMade = true;
                            game.setPlayer(game.getXMove(), this);
                        }
                    }
                }
            } else if (from == FROM_STORAGE_CARDS) {
                if (line >= 0 && line < 4) {
                    Stack<Integer> temp = storage.get(line);
                    if (temp.lastElement() == game.getBoard().getValue(to) + 1 || temp.lastElement() == 13) {
                        if(temp.lastElement() == game.getBoard().getValue(to) + 1) {
                            game.getBoard().setIsSkipBoCard(false, to);
                        }
                        else if(temp.lastElement() == 13) {
                            game.getBoard().setIsSkipBoCard(true, to);
                        }
                        game.getBoard().setValue(to, game.getBoard().getValue(to) + 1);
                        temp.pop();
                        isMade = true;
                        game.setPlayer(game.getXMove(), this);
                    }
                }
            } else if (from == FROM_CURRENT_CARDS) {
                if (line >= 0 && line < currentCards.size()) {
                    if (currentCards.get(line) == game.getBoard().getValue(to) + 1 || currentCards.get(line) == 13) {
                        if(currentCards.get(line) == game.getBoard().getValue(to) + 1) {
                            game.getBoard().setIsSkipBoCard(false, to);
                        }
                        else if(currentCards.get(line) == 13) {
                            game.getBoard().setIsSkipBoCard(true, to);
                        }
                        game.getBoard().setValue(to, game.getBoard().getValue(to) + 1);
                        currentCards.remove(line);
                        isMade = true;
                        game.setPlayer(game.getXMove(), this);

                        if(currentCards.size() == 0)
                            addCurrentCards();
                    }
                }
            }
        }
        else if(usedFor == MOVE_END) {
            int currentLine = code.charAt(0) - '0';
            int storageLine = code.charAt(1) - '0';
            storage.get(storageLine).add(currentCards.get(currentLine));
            currentCards.remove(currentLine);
            isMade = true;
            game.setPlayer(game.getXMove(), this);
            game.setXMove((game.getXMove() + 1) % game.getSettings().getPlayersCount());
            game.getPlayer(game.getXMove()).addCurrentCards();
        }
        return isMade;
    }

    public void allMoves(Game game) {
        int valid = 0;
        do {
            valid = 0;
            for (int i = 0; i < 4; i++) {
                if (mainCards.get(0) == game.getBoard().getValue(i) + 1) {
                    if (Math.random() < 0.9) {
                        makeMove("1*" + i, 10, game);
                        valid++;
                    }
                    break;
                }
            }
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < currentCards.size(); j++) {
                    if (currentCards.get(j) == game.getBoard().getValue(i) + 1) {
                        if (Math.random() < 0.9) {
                            makeMove("2" + j + i, 10, game);
                            valid++;
                        }
                        break;
                    }
                }

            }
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (storage.get(j).lastElement() == game.getBoard().getValue(i) + 1) {
                        if (Math.random() < 0.9) {
                            makeMove("3" + j + i, 10, game);
                            valid++;
                        }
                        break;
                    }
                }
            }
        }
        while(valid != 0);

        makeMove((int)(Math.random()*currentCards.size()) + "" + (int)(Math.random()*4), 11, game);
    }

    public ArrayList<Integer> getMainCards() {
        return mainCards;
    }

    public ArrayList<Stack<Integer>> getStorage() {
        return storage;
    }

    public ArrayList<Integer> getCurrentCards() {
        return currentCards;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
