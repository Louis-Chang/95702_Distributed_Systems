package ds.cmu.task1;

public class Card {
    private String code;
    private String value;
    private String suit;

    public Card(String code, String value, String suit) {
        this.code = code;
        this.value = value;
        this.suit = suit;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }
}
