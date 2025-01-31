package blackjack.domain

import blackjack.model.Card
import blackjack.model.CardType

const val BLACKJACK_SCORE = 21

class Cards(
    initialValue: List<Card> = emptyList(),
) {
    private val _value: MutableList<Card> =
        MutableList(initialValue.size) { index -> initialValue[index].copy() }

    val value: List<Card>
        get() = _value.toList()

    val size: Int
        get() = _value.size

    fun shuffle() = _value.shuffle()

    fun add(card: Card) = _value.add(card)
    fun sum(): Int =
        if (shouldCountWithAce()) {
            sumWithAce()
        } else {
            _value.sumOf { it.type.score }
        }

    fun takeOutCard(): Card {
        require(_value.isNotEmpty()) { "잘못된 호출입니다. 먼저 카드 목록에 카드를 채우세요." }
        return _value.removeFirst()
    }

    private fun shouldCountWithAce(): Boolean = _value.any { it.type == CardType.ACE }

    private fun sumWithAce(): Int {
        val filteredCardsSum = filterNotAceCard().sumOf { it.type.score }
        return filteredCardsSum.takeIf { BLACKJACK_SCORE - it < CardType.ACE.specialScore }
            ?.let { it + CardType.ACE.score }
            ?: run { filteredCardsSum + CardType.ACE.specialScore }
    }

    private fun filterNotAceCard(): List<Card> = _value.filterNot { it.type == CardType.ACE }
}
