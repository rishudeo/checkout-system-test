package checkout.model

sealed trait Offer[T <: Product] {
  def product: T
  def qualifyingAmount: Int
  def discount: BigDecimal
}

object Offer {

  case class BuyOneGetOneFree[T <: Product](product: T) extends Offer[T] {
    override val qualifyingAmount: Int = 2
    override val discount: BigDecimal  = product.price
  }

  case class ThreeForTwo[T <: Product](product: T) extends Offer[T] {
    override val qualifyingAmount: Int = 3
    override val discount: BigDecimal  = product.price
  }

}
