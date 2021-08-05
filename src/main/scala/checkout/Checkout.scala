package checkout

import checkout.model.{Error, Offer, Product}

/**
  * A checkout service that can be used to calculate the price of products
  * in a shopping cart.
  */
trait Checkout {

  /**
    * Calculates the price of products within a shopping cart.  It will first parse the
    * names of the products and then calculate the price.
    *
    * @param cart a list of items in a shopping cart
    * @return the price of the sum of the items, or an error if an item is not recognised.
    */
  def calculatePrice(cart: List[String]): Either[Error, BigDecimal]
}

/**
  * The default implementation of a Checkout service
  *
  * @param offers the offers that can be applied to a shopping cart
  */
class CheckoutImpl(offers: List[Offer[_ <: Product]] = Nil) extends Checkout {

  override def calculatePrice(cart: List[String]): Either[Error, BigDecimal] =
    parseProducts(cart).map { products =>
      totalPrice(products) - discountFromOffers(products)
    }

  private def parseProducts(cart: List[String]): Either[Error, List[Product]] = {
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
