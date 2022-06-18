package blackjack.domain.participant.player

import blackjack.domain.card.CardDeckTest
import blackjack.domain.participant.dealer.Dealer
import blackjack.domain.participant.vo.BetAmount
import blackjack.domain.participant.vo.CardsInHand
import blackjack.domain.participant.vo.Name
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

class PlayersTest : StringSpec({
    "참가자들을 생성할수 있다." {
        shouldNotThrow<Throwable> {
            Players(
                listOf(
                    Player.sit(Name("dean"), BetAmount.of(1_000)),
                    Player.sit(Name("dane"), BetAmount.of(1_000))
                )
            )
        }
    }

    "참가자 수가 2명 미만일 경우 Exception을 던진다." {
        val players = listOf(
            listOf(Player.sit(Name("dean"), BetAmount.of(1_000))),
            emptyList()
        )

        players.forAll {
            shouldThrow<IllegalArgumentException> {
                Players(it)
            }
        }
    }

    "블랙젝 게임 준비를 할수 있다." {
        val players =
            Players(listOf(Player.sit(Name("dean"), BetAmount.of(1_000)), Player.sit(Name("dane"), BetAmount.of(1_000))))
        val cardDeck = CardDeckTest.cardDeck()

        players.ready(cardDeck)

        players.players.forAll {
            it.cardsInHand.cards.size shouldBe 2
        }
    }

    "최종 승패를 알수 있다." {
        val players =
            Players(listOf(Player.sit(Name("dean"), BetAmount.of(1_000)), Player.sit(Name("dane"), BetAmount.of(1_000))))
        val dealer = Dealer(CardsInHand())

        shouldNotThrow<Throwable> { players.score(PlayerScoreStrategy(dealer)) }
    }
})