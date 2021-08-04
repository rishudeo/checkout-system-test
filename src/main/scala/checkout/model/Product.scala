package checkout.model

sealed trait Product {
  def name: String
  def price: BigDecimal
}

object Product {

  case object Apple extends Product {
    val name: String      = "Apple"
    val price: BigDecimal = 0.60
  }

  case object Orange extends Product {
    val name: String      = "Orange"
    val price: BigDecimal = 0.25
  }

  val AllProductsByName: Map[String, Product] =
    List(Apple, Orange)
      .map(p => (p.name, p))
      .toMap

  def apply(name: String): Option[Product] = AllProductsByName.get(name)

}
