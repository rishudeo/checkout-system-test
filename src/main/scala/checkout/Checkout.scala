package checkout

import checkout.model.{Error, Product}

trait Checkout {
  def calculatePrice(cart: List[String]): Either[Error, BigDecimal]
}

class CheckoutImpl extends Checkout {

  override def calculatePrice(cart: List[String]): Either[Error, BigDecimal] = {

    val (errors, products) =
      cart
        .map { name =>
          Product(name).toRight(Error(s"Unknown item: $name"))
        }
        .partitionMap(identity)

    errors.headOption match {
      case Some(error) => Left(error)
      case _           => Right(products.map(_.price).sum)
    }
  }

}
