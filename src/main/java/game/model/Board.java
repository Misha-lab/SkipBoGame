package game.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
    private ArrayList<Integer> field;
    private ArrayList<Boolean> isSkipBoCard;

    public Board() {
        field = new ArrayList<>();
        isSkipBoCard = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            field.add(0);
            isSkipBoCard.add(false);
        }
    }

    public Board(String string) {
        String[] parameters = string.split(" ");
        field = new ArrayList<>();
        isSkipBoCard = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            field.add(Integer.parseInt(parameters[i]));

            if(Integer.parseInt(parameters[i+4]) == 1)
                isSkipBoCard.add(true);
            else isSkipBoCard.add(false);
        }

    }

    public String toString() {
        String result = "";
        for (int i = 0; i < 4; i++) {
            result += field.get(i) + " ";
        }
        for (int i = 0; i < 4; i++) {
            int value = 0;
            if(isSkipBoAt(i))
                value = 1;
            if(i == 3) {
                result += value;
            }
            else result += value + " ";
        }
        return result;
    }

    public void setValue(int line, int value) {
        if(line >= 0 && line < 4) {
            field.set(line, value);
        }
    }

    public int getValue(int line) {
        if(line>=0 && line < 4) {
            return field.get(line);
        }
        else return -1;
    }

    public boolean isSkipBoAt(int line) {
        if(line>=0 && line < 4) {
            return isSkipBoCard.get(line);
        }
        else return false;
    }

    public void setIsSkipBoCard(boolean flag, int line) {
        if(line >= 0 && line < 4) {
            isSkipBoCard.set(line, flag);
        }
    }

    public boolean clear12() {
        boolean isCleared = false;
        for (int i = 0; i < 4; i++) {
            if(getValue(i) == 12) {
                setValue(i, 0);
                isCleared = true;
            }
        }
        return isCleared;
    }
}
