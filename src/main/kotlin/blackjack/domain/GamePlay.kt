package blackjack.domain

import blackjack.domain.Finished.Blackjack
import blackjack.domain.Finished.Bust
import blackjack.domain.Finished.Stay
import blackjack.domain.Game.Companion.INITIAL_CARDS_COUNT
import blackjack.domain.Playing.Hit
import blackjack.model.Card

open class GamePlay(
    initialState: State = Started()
) : Play {

    private var state: State = initialState
    override val finished: Boolean
        get() = state.finished
    override val hit: Boolean
        get() = state is Hit
    override val stay: Boolean
        get() = state is Stay
    override val bust: Boolean
        get() = state is Bust
    override val blackjack: Boolean
        get() = state is Blackjack

    override val cards: Cards
        get() = state.cards

    override fun shouldBeReadyToPlay(): Boolean =
        state.cards.size == INITIAL_CARDS_COUNT && state !is Started

    override fun draw(card: Card) {
        state = state.draw(card)
    }

    override fun stay() {
        state = state.stay()
    }

    override fun score(): Int = state.cards.sum()
}

class DealerGamePlay: GamePlay() {
    override fun draw(card: Card) {
        super.draw(card)
        if (shouldStay()) stay()
    }
    private fun shouldStay(): Boolean = hit && score() >= STAY_SCORE

    companion object {
        private const val STAY_SCORE = 17
    }
}

interface Play {
    val finished: Boolean
    val hit: Boolean
    val stay: Boolean
    val bust: Boolean
    val blackjack: Boolean

    val cards: Cards

    fun shouldBeReadyToPlay(): Boolean
    fun draw(card: Card)
    fun stay()
    fun score(): Int
}
