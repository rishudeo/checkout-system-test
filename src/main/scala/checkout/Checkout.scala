package checkout

import checkout.model.{Error, Offer, Product}

trait Checkout {
  def calculatePrice(cart: List[String]): Either[Error, BigDecimal]
}

class CheckoutImpl(offers: List[Offer[_ <: Product]] = Nil) extends Checkout {

  override def calculatePrice(cart: List[String]): Either[Error, BigDecimal] =
    parseProducts(cart).map { products =>
      totalPrice(products) - discountFromOffers(products)
    }

  def parseProducts(cart: List[String]): Either[Error, List[Product]] = {
    val (errors, products) =
      cart
        .map { name =>
          Product(name).toRight(Error(s"Unknown item: $name"))
        }
        .partitionMap(identity)

    errors.headOption match {
      case Some(error) => Left(error)
      case _           => Right(products)
    }
  }

  private def totalPrice(products: List[Product]): BigDecimal =
    products.map(_.price).sum

  private def discountFromOffers(products: List[Product]): BigDecimal =
    offers.map { offer =>
      val qualifyingProducts = products.count(_ == offer.product)

      val offerMultiple = Math.abs(qualifyingProducts / offer.qualifyingAmount)

      offer.discount * offerMultiple
    }.sum

}
