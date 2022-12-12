package blackjack.model

enum class CardType(val value: String, val score: Int, val specialScore: Int = 0) {
    ACE("A", 1, 11),
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("J", 10),
    QUEEN("Q", 10),
    KING("K", 10),
    ;
}

enum class CardShape(val text: String) {
    SPADE("스페이드"),
    HEART("하트"),
    DIAMOND("다이아"),
    CLOVER("클로버"),
    ;
}
