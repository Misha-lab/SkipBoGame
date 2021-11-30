package SB;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import SB.model.*;
import java.util.ArrayList;

public class TestRunner {
    @Test
    public void isSkipBoAtTest() {
        Board board = new Board();
        board.setIsSkipBoCard(true, 2);
        assertTrue(!board.isSkipBoAt(0) && board.isSkipBoAt(2));
        //check(!board.isSkipBoAt(0) && board.isSkipBoAt(2));
    }

    @Test
    public void clear12Test() {
        Board board = new Board();
        board.setValue(0, 7);
        board.setValue(1, 3);
        board.setValue(2, 12);
        board.setValue(3, 12);

        board.clear12();

        //check(board.getValue(2) == 0 && board.getValue(3) == 0);
        assertTrue(board.getValue(2) == 0 && board.getValue(3) == 0);
    }

    @Test
    public void updateGameStatusTest() {
        Game game = new Game(new GameSettings(0, 7, 2));
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(0));
        players.add(new Player(1));
        game.setPlayers(players);

        game.getPlayers().get(0).generateCards(7);
        game.getPlayers().get(1).generateCards(7);
        game.setGameStatus(Game.IN_GAME);
        int before = game.getGameStatus();
        game.getPlayers().get(0).getMainCards().clear();
        game.updateGameStatus();
        int after = game.getGameStatus();

        //check(before != after);
        assertTrue(before != after);
    }

    @Test
    public void generateCardsTest() {
        Player player = new Player(0);
        player.generateCards(7);
        int fails = 0;
        for (int i = 0; i < 7; i++) {
            if(player.getMainCards().get(i) == 0)
                fails++;
        }

        for (int i = 0; i < 5; i++) {
            if(player.getCurrentCards().get(i) == 0)
                fails++;
        }

        //check(fails == 0);
        assertTrue(fails == 0);
    }

    @Test
    public void addCurrentCardsTest() {
        Player player = new Player(1);
        player.addCurrentCards();
        //check(player.getCurrentCards().size() == 5);
        assertTrue(player.getCurrentCards().size() == 5);
    }

    @Test
    public void makeMoveTest() {
        Game game = new Game(new GameSettings(0, 7, 2));
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(0));
        players.add(new Player(1));
        game.setPlayers(players);

        game.getPlayers().get(0).generateCards(7);
        game.getPlayers().get(1).generateCards(7);

        Board board = game.getBoard();
        board.setValue(0,5);
        Player player = new Player(2);
        player.generateCards(7);
        for (int i = 0; i < 5; i++) {
            player.getCurrentCards().set(i, i + 5);
        }
        for (int i = 0; i < 3; i++) {
            player.getMainCards().set(i, i + 7);
        }

        boolean res = !player.makeMove("300", Player.MOVE, game)
                && player.makeMove("310", Player.MOVE, game)
                && player.makeMove("1*0", Player.MOVE, game);

        //check(res);
        assertTrue(res);
    }
}
