package dev.handeul.autto.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Round {
    private static final int LEN_NUMBERS = 6;

    private final int id;
    private final List<Integer> numbers;
    private final int bonusNum;
    private final Date date;

    public Round(int id, Date date) {
        this.id = id;
        this.numbers = null;
        this.bonusNum = -1;
        this.date = date;
    }

    public Round(int id, int[] numbers, int bonusNum, Date date) {
        this.id = id;
        this.numbers = new ArrayList<>();
        this.bonusNum = bonusNum;
        this.date = date;

        if(numbers.length == LEN_NUMBERS) {
            for(int i=0; i<LEN_NUMBERS; i++) {
                this.numbers.add(numbers[i]);
            }
        }
    }

    public int getId() {
        return id;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public int getBonusNum() {
        return bonusNum;
    }

    public Date getDate() {
        return date;
    }
}
