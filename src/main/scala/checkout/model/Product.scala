package checkout.model

/**
  * A Product that can be sold in the store
  *
  * @param name the name of the product
  * @param price the price of the product
  */
sealed abstract class Product(val name: String, val price: BigDecimal)

object Product {

  case object Apple  extends Product("Apple", 0.60)
  case object Orange extends Product("Orange", 0.25)

  val AllProductsByName: Map[String, Product] =
    List(Apple, Orange)
      .map(p => (p.name, p))
      .toMap

  def apply(name: String): Option[Product] = AllProductsByName.get(name)

}
