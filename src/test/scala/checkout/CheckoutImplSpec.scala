package checkout

import checkout.model.Error
import checkout.model.Offer.{BuyOneGetOneFree, ThreeForTwo}
import checkout.model.Product.{Apple, Orange}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CheckoutImplSpec extends AnyWordSpec with Matchers {

  "Checkout" should {

    "Calculate the price of items" in {
      val checkout = new CheckoutImpl()

      checkout.calculatePrice(Nil) mustBe Right(BigDecimal(0))
      checkout.calculatePrice(List("Apple")) mustBe Right(BigDecimal(0.60))
      checkout.calculatePrice(List("Orange")) mustBe Right(BigDecimal(0.25))
      checkout.calculatePrice(
        List("Apple", "Apple", "Orange", "Apple")
      ) mustBe Right(BigDecimal(2.05))
    }

    "return an error if an item is not recognised" in {
      val checkout = new CheckoutImpl()

      checkout.calculatePrice(List("Banana")) mustBe Left(Error("Unknown item: Banana"))
    }

    "apply an offer to a qualifying purchase" in {
      val offers = List(
        BuyOneGetOneFree(Apple),
        ThreeForTwo(Orange)
      )
      val checkout = new CheckoutImpl(offers)

      checkout.calculatePrice(List("Apple", "Apple")) mustBe Right(BigDecimal(0.60))
      checkout.calculatePrice(List("Orange", "Orange", "Orange")) mustBe Right(BigDecimal(0.50))
      checkout.calculatePrice(List("Apple", "Apple", "Orange", "Orange", "Orange")) mustBe Right(BigDecimal(1.10))
    }

    "not apply an offer if the purchase does not qualify" in {
      val offers = List(
        BuyOneGetOneFree(Apple),
        ThreeForTwo(Orange)
      )
      val checkout = new CheckoutImpl(offers)

      checkout.calculatePrice(List("Apple")) mustBe Right(BigDecimal(0.60))
      checkout.calculatePrice(List("Orange", "Orange")) mustBe Right(BigDecimal(0.50))
    }

    "apply an offer multiple times to a qualifying purchase" in {
      val offers = List(
        BuyOneGetOneFree(Apple),
        ThreeForTwo(Orange)
      )
      val checkout = new CheckoutImpl(offers)

      checkout.calculatePrice(List("Apple", "Apple", "Apple", "Apple")) mustBe Right(BigDecimal(1.20))
      checkout.calculatePrice(List("Orange", "Orange", "Orange", "Orange", "Orange", "Orange")) mustBe Right(
        BigDecimal(1.00)
      )
    }

  }

}
