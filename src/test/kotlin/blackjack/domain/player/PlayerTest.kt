package blackjack.domain.player

import blackjack.domain.card.Ace
import blackjack.domain.card.Card
import blackjack.domain.card.Cards
import blackjack.domain.card.Jack
import blackjack.domain.card.NumberCard
import blackjack.domain.card.Queen
import blackjack.domain.card.Suit
import blackjack.domain.score.Score
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class PlayerTest : DescribeSpec({

    describe("constructor") {
        context("이름이 주어지면") {
            it("게임 참가자를 생성한다") {
                Player("요한") shouldNotBe null
            }
        }
    }

    describe("changeStatus") {
        context("STAY 또는 BUST가 아닌 경우") {
            it("카드를 추가하기로 할 수 있다") {
                val player = Player(name = "요한")
                player.changeStatus(PlayerStatus.HIT)
            }

            it("카드를 추가하지 않기로 할 수 있다") {
                val player = Player(name = "요한")
                player.changeStatus(PlayerStatus.STAY)
            }
        }

        context("BUST 인 경우") {
            val player = Player(
                name = "요한",
                cards = Cards(
                    listOf(
                        Card(Suit.DIAMOND, Queen()),
                        Card(Suit.DIAMOND, NumberCard(10)),
                        Card(Suit.DIAMOND, NumberCard(2)),
                    )
                )
            )

            it("카드를 추가하기로 하면 IllegalStateException 이 발생한다") {
                shouldThrow<IllegalStateException> {
                    player.changeStatus(PlayerStatus.HIT)
                }
            }

            it("카드를 추가히지 않기로 하면 IllegalStateException 이 발생한다") {
                shouldThrow<IllegalStateException> {
                    player.changeStatus(PlayerStatus.STAY)
                }
            }
        }
    }

    describe("addCard") {
        context("STAY 또는 BUST 가 아닌 경우") {
            it("카드를 추가할 수 있다") {
                val player = Player(
                    name = "요한",
                    cards = Cards(
                        listOf(
                            Card(Suit.DIAMOND, Queen()),
                            Card(Suit.DIAMOND, NumberCard(9)),
                            Card(Suit.DIAMOND, NumberCard(2)),
                        )
                    )
                )

                player.addCard(Card(Suit.DIAMOND, NumberCard(10)))

                player.cards.cards shouldBe
                    listOf(
                        Card(Suit.DIAMOND, Queen()),
                        Card(Suit.DIAMOND, NumberCard(9)),
                        Card(Suit.DIAMOND, NumberCard(2)),
                        Card(Suit.DIAMOND, NumberCard(10)),
                    )
            }
        }

        context("BUST 인 경우") {
            it("IllegalStateException 이 발생한다") {
                val player = Player(
                    name = "요한",
                    cards = Cards(
                        listOf(
                            Card(Suit.DIAMOND, Queen()),
                            Card(Suit.DIAMOND, NumberCard(10)),
                            Card(Suit.DIAMOND, NumberCard(2)),
                        )
                    )
                )

                shouldThrow<IllegalStateException> {
                    player.addCard(Card(Suit.DIAMOND, NumberCard(2)))
                }
            }
        }

        context("STAY 인 경우") {
            it("IllegalStateException 이 발생한다") {
                val player = Player(
                    name = "요한",
                    playerStatus = PlayerStatus.STAY
                )

                shouldThrow<IllegalStateException> {
                    player.addCard(Card(Suit.DIAMOND, NumberCard(2)))
                }
            }
        }
    }

    describe("score") {
        it("카드들의 점수의 합을 구할 수 있다") {
            val player = Player(
                name = "요한",
                cards = Cards(
                    listOf(
                        Card(Suit.DIAMOND, Ace()),
                        Card(Suit.DIAMOND, Jack()),
                        Card(Suit.DIAMOND, NumberCard(5)),
                    )
                )
            )

            player.cards.score() shouldBe Score(16)
        }
    }

    describe("isEnd") {
        it("BUST 이거나 STAY 가 아니면 참여가 종료되지 않는다") {
            val player = Player(name = "yohan")

            player.isEnd() shouldBe false
        }

        it("STAY 이면 참가자의 참여가 종료된다") {
            val player = Player(name = "yohan", playerStatus = PlayerStatus.STAY)

            player.isEnd() shouldBe true
        }

        it("BUST 이면 참가자의 참여가 종료된다") {
            val player = Player(
                name = "yohan",
                cards = Cards(
                    listOf(
                        Card(Suit.DIAMOND, Queen()),
                        Card(Suit.DIAMOND, NumberCard(9)),
                        Card(Suit.DIAMOND, NumberCard(3)),
                    )
                )
            )

            player.isEnd() shouldBe true
        }
    }
})