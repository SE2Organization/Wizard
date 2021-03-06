package com.github.wizard.game;

import com.github.wizard.api.Card;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Deck {

    protected static final List<Card.Color> validColors;
    protected static final List<Card.Value> validNumbers;

    public static final List<Card> allCards;

    static {
        validColors =
                Arrays.stream(Card.Color.values())
                        .filter(color -> color != Card.Color.UNRECOGNIZED)
                        .toList();

        validNumbers =
                Arrays.stream(Card.Value.values())
                        .filter(
                                v ->
                                        v != Card.Value.UNRECOGNIZED
                                                && v != Card.Value.WIZARD
                                                && v != Card.Value.JESTER)
                        .toList();

        allCards = Collections.unmodifiableList(generateAllCards());
    }

    private static List<Card> generateNumberedCards() {
        return validColors.stream()
                .flatMap(
                        color ->
                                validNumbers.stream()
                                        .map(
                                                number ->
                                                        Card.newBuilder()
                                                                .setColor(color)
                                                                .setValue(number)
                                                                .build()))
                .toList();
    }

    private static List<Card> generateWizards() {
        return validColors.stream()
                .map(color -> Card.newBuilder().setColor(color).setValue(Card.Value.WIZARD).build())
                .toList();
    }

    private static List<Card> generateJesters() {
        return validColors.stream()
                .map(color -> Card.newBuilder().setColor(color).setValue(Card.Value.JESTER).build())
                .toList();
    }

    private static List<Card> generateAllCards() {
        return Stream.of(generateJesters(), generateWizards(), generateNumberedCards())
                .flatMap(List::stream)
                .toList();
    }

    public int getCardsAvailable() {
        return allCards.size();
    }

    private final Queue<Card> cards;

    public Deck() {
        cards = new LinkedList<>(allCards);
    }

    public void shuffle() {
        cards.clear();
        cards.addAll(allCards);

        Collections.shuffle((List<?>) cards);
    }

    public Card draw() {
        return cards.remove();
    }

    public List<Card> draw(int amount) {
        return IntStream.range(0, amount).mapToObj(i -> draw()).toList();
    }

    public int size() {
        return cards.size();
    }
}
