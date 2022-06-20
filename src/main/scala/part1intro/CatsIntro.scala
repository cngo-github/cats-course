package part1intro

object CatsIntro {
  // Eq - compare values at compile to check for the same type.
  // val aComparison = 2 == "a string" -- wrong, will trigger a compiler warning. This will always return false.

  // Part 1
  import cats.Eq

  // Part 2 - Import TC instances for the needed types.
  import cats.instances.int._

  // Part 3 - use the type class API.
  val intEquality = Eq[Int]
  val  aTypeSafeComparison = intEquality.eqv(2, 3)
  //val anUnsafeComparison = intEquality.eqv(2, "a string") -- doesn't compile!'

  // Part 4 - use extension methods if applicable
  import cats.syntax.eq._

  val anotherTypeSafeComparison = 2 === 3  // false
  val notEqualsComparison = 2 =!= 3 // true
  // Extension methods are only visible in the presence of the right TC instance.

  // Part 5 - extending the TC operations to composite types (e.g., List)
  import cats.instances.list._ // Bring in Eq[List] in scope

  val aListComparison = List(2) === List(3) // false

  // Part 6 - Create a TC instance for a custom type
  case class ToyCar(model: String, price: Double)

  implicit val toyCarEq: Eq[ToyCar] = Eq.instance[ToyCar]{(car1, car2) =>
   car1.price == car2.price
  }

  val compareTwoToyCars = ToyCar("Ferrari", 29.99) === ToyCar("Lamborghini", 29.99) // true

  // Bring in everything Cats
  import cats._
  import cats.implicits._

  def main(args: Array[String]): Unit = {}
}
