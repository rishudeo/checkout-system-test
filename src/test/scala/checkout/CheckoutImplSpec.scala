package checkout

import checkout.model.Error
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

  }

}
