package game.model;

import java.util.ArrayList;
import java.util.Stack;

public class Bot {
    public static final int FROM_MAIN_CARDS = 1;
    public static final int FROM_STORAGE_CARDS = 2;
    public static final int FROM_CURRENT_CARDS = 3;
    public static final int MOVE = 10;
    public static final int MOVE_END = 11;

    protected ArrayList<Integer> mainCards;
    protected ArrayList<Stack<Integer>> storage;
    protected ArrayList<Integer> currentCards;
    protected int id;

    public Bot(int id) {
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
                            game.setBot(game.getXMove(), this);
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
                        game.setBot(game.getXMove(), this);
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
                        game.setBot(game.getXMove(), this);

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
            game.setBot(game.getXMove(), this);
            game.setXMove((game.getXMove() + 1) % game.getSettings().getPlayersCount());
            game.getBot(game.getXMove() % game.getSettings().getPlayersCount()).addCurrentCards();
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
                        makeMove("1*", 10, game);
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
