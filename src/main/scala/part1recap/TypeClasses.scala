package part1recap

object TypeClasses {
  // Type Classes (TC) are programming patterns to enhance types with additional capabilities.
  case class Person(name: String, age: Int)

  // Part 1 - TC definition (e.g., trait, abstract trait, or abstract class)
  trait JsonSerializer[T] {
    def toJson(value: T): String
  }

  // Part 2 - Create implicit TC instances (i.e., concrete implementations of the TC for the different supported types).
  implicit object StringSerializer extends JsonSerializer[String] {
    override def toJson(value: String): String = "\"" + value + "\""
  }

  implicit object IntSerializer extends  JsonSerializer[Int] {
    override def toJson(value: Int): String = value.toString
  }

  implicit object PersonSerializer extends JsonSerializer[Person] {
    override def toJson(value: Person): String =
      s"""
        |{ "name" : ${value.name}, "age" : ${value.age}}
        |""".stripMargin.trim
  }

  // Part 3 - offer some API
  def convertListToJson[T](list: List[T])(implicit serializer: JsonSerializer[T]): String =
    list.map(elem => serializer.toJson(elem)).mkString("[", ", ", "]")

  // Part 4 - Extend the existing types via extension methods
  object JsonSyntax {
    implicit class JsonSerializable[T](value: T)(implicit serializer: JsonSerializer[T]) {
      def toJson: String = serializer.toJson(value)
    }
  }

  def main(args: Array[String]): Unit = {
    println(convertListToJson(List(Person("Alice", 23), Person("Xavier", 45))))
    println(convertListToJson(List("Hello", "World")))
    println(convertListToJson(List(1, 2, 3)))

    val bob = Person("Bob", 35)
    import JsonSyntax._
    println(bob.toJson)
  }
}
