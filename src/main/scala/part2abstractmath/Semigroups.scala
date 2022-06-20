package part2abstractmath

object Semigroups {
  // Semigroups combine elements of the same type.
  import cats.Semigroup
  import cats.instances.int._

  val naturalIntSemigroup = Semigroup[Int]
  val intCombination = naturalIntSemigroup.combine(2, 46) // addition

  import cats.instances.string._

  // Specific API.
  val naturalStringSemigroup = Semigroup[String]
  val stringCombination = naturalStringSemigroup.combine("I love ", "Cats") // concatenation

  def reduceInts(list: List[Int]): Int = list.reduce(naturalIntSemigroup.combine)
  def reduceStrings(list: List[String]): String = list.reduce(naturalStringSemigroup.combine)

  // General API
  def reduceThings[T](list: List[T])(implicit semigroup: Semigroup[T]): T = list.reduce(semigroup.combine)

  def main(args: Array[String]): Unit = {
    println(intCombination)
    println(stringCombination)

    // Specific API.
    println(reduceInts((1 to 10).toList))
    println(reduceStrings(List("I'm ", "starting ", "to ", "like ", "semigroups!")))

    // General API.
    println(reduceThings((1 to 10).toList)) // Compiler injects the implicit Semigroup[Int]
    println(reduceThings(List("I'm ", "starting ", "to ", "like ", "semigroups!"))) // Compiler injects the implicit
                                                                                    // Semigroup[String]

    import cats.instances.option._ // Compiler will produce an implicit Semigroup[Option[Int]]
    val numberOptions: List[Option[Int]] = (1 to 10).toList.map(n => Option(n))
    println(reduceThings(numberOptions))

    // Extension methods for semigroup - |+|
    import  cats.syntax.semigroup._

    // TODO: Support a new type without modifying existing code.
    case class Expense(id: Long, amount: Double)
    implicit val ExpenseSemigroup: Semigroup[Expense] = Semigroup.instance[Expense] {(expense1, expense2) =>
      Expense(Math.max(expense1.id, expense2.id) + 1, expense1.amount + expense2.amount)
    }
    val expenses: List[Expense] = (1 to 10).toList.map(n => Expense(n, n))
    println(reduceThings(expenses))

    val anIntSum = 2 |+| 3 // 2 combine with 3 = 5 -- requires the presence of an implicit Semigroup[Int]
    val aStringConcat = " I like " |+| "semigroups" // I like semigroups
    val aCombinedExpense = Expense(4, 80) |+| Expense(56, 46) // Expense(56, 126)

    // TODO: Implement reduceThings2 with |+|
    def reduceThings2[T](list: List[T])(implicit semigroup: Semigroup[T]): T = list.reduce(_ |+| _)

    def reduceThings3[T :Semigroup](list: List[T]): T = list.reduce(_ |+| _)

    println(reduceThings2(expenses))
    println(reduceThings3(expenses))
  }
}
