package part1recap

object Implicits {
  // Implicit classes: one-argument wrapper over values.
  case class Person(name: String) {
    def greet: String = s"Hi, my name is $name!"
  }

  implicit class ImpersonableString(name: String) {
    def greet: String = Person(name).greet // Constructs a person and then calls greet.
  }

  val impersonableString = new  ImpersonableString("Peter") // Explicit method.
  impersonableString.greet

  val greeting = "Peter".greet // Equivalent to the above.

  // Importing implicit conversions in scope.
  import  scala.concurrent.duration._ // Import everything from scala.concurrent.duration
  val oneSec = 1.second

  // Implicit arguments and values.
  def increment(x: Int)(implicit amount: Int) = x + amount
  implicit val defaultAmount = 10
  val incremented2 = increment(2) // 2 + 10 = 12
                                  // equivalent of increment(2)(10) where the implicit argument is passed automatically
                                  // by the compiler.

  def multiply(x: Int)(implicit times: Int) = x * times
  val times2 = multiply(2) // 2 * 10 = 2

  // More complex example.
  trait JsonSerializer[T] {
    def toJson(value: T): String
  }

  def listToJson[T](list: List[T])(implicit serializer: JsonSerializer[T]): String =
    list.map(value => serializer.toJson(value)).mkString("[", ",", "]")

  implicit val personSerializer: JsonSerializer[Person] = new JsonSerializer[Person] {
    override def toJson(value: Person): String =
      s"""
        |{"name" : "${value.name}"}
        |""".stripMargin
  }

  val personJson = listToJson(List(Person("Alice"), Person("Bob")))
  // An implicit argument is used to prove the existence of a type.

  // Implicit methods.
  implicit def oneArgCaseClassSerializer[T <: Product /* T derives from Product. */]: JsonSerializer[T] =
    new JsonSerializer[T] {
      override def toJson(value: T): String =
        s"""
          |"${value.productElementName(0)}" : "${value.productElement(0)}"
          |""".stripMargin.trim
    }

  case class Cat(name: String)
  val catsToJson = listToJson(List(Cat("Tom"), Cat("Garfield")))
  // In the background, this will be:
  // val catsToJson = listToJson(List(Cat("Tom"), Cat("Garfield")))(oneArgCaseClassSerializer[Cat])
  // Implicit methods are use to prove the existence of a type.

  // Implicit methods can be used as a conversion (DISCOURAGED), use an implicit class instead.

  // You must have exactly one implicit of a type in scope.
  // Compilers search for implicits in:
  //  1. the local scope = clearly defined implicit vals/defs/classes
  //  2. the imported scope
  //  3. the companion objects of the type involved in the method call.

  def main(args: Array[String]): Unit = {
    println(oneArgCaseClassSerializer[Cat].toJson(Cat("Garfield")))
    println(oneArgCaseClassSerializer[Person].toJson(Person("David")))
  }
}
