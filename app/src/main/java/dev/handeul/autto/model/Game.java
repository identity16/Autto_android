package dev.handeul.autto.model;

public class Game {
    public enum State {
        UNKNOWN, FIRST, SECOND, THIRD, FOURTH, FIFTH, FAIL
    }

    private final int roundId;
    private final Integer[] numbers;
    private final State state;

    public Game(int roundId, Integer[] numbers, String strState) {
        this(roundId, numbers, getStateFromString(strState));
    }

    public Game(int roundId, Integer[] numbers, State state) {
        this.roundId = roundId;
        this.numbers = numbers;
        this.state = state;
    }

    public Integer[] getNumbers() {
        return numbers;
    }

    private static State getStateFromString(String strState) {
        String str = strState.trim();
        switch (str) {
            case "자동":
            case "수동":
                return State.UNKNOWN;
            case "낙첨":
                return State.FAIL;
            case "5등":
                return State.FIFTH;
            case "4등":
                return State.FOURTH;
            case "3등":
                return State.THIRD;
            case "2등":
                return State.SECOND;
            case "1등":
                return State.FIRST;
            default:
                return null;
        }
    }
}
