package dev.handeul.autto.model;

public class Game {
    public static class State {
        public static final String UNKNOWN = "미추첨";
        public static final String FIRST = "1등";
        public static final String SECOND = "2등";
        public static final String THIRD = "3등";
        public static final String FOURTH = "4등";
        public static final String FIFTH = "5등";
        public static final String FAIL = "낙첨";

        private State() {}
    }

    private final int roundId;
    private final Integer[] numbers;
    private final String state;

    public Game(int roundId, Integer[] numbers, String state) {
        this.roundId = roundId;
        this.numbers = numbers;
        this.state = getStateFromString(state);
    }

    public int getRoundId() {
        return roundId;
    }

    public Integer[] getNumbers() {
        return numbers;
    }

    public String getState() {
        return state;
    }

    private static String getStateFromString(String strState) {
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
