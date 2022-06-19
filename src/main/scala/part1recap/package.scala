package part1recap

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object Essentials {
  val aBoolean: Boolean = false

  // expressions are evaluated to a value.
  val anIfExpression = if (2 > 3) "bigger" else "smaller"

  // instructions are expressions that return Unit (i.e., it returns nothing meaningful.
  val theUnit = println("Hello, Scala") // Unit == "void" == ()
  // Side effects - actions that do not evaluate to a meaningful value, but the computer still does something (e.g.,
  // println)

  // OOP
  class Animal
  class Cat extends Animal // Inherits non-private fields and methods.
  trait Carnivore { // "Interface" that can contain unimplemented fields and methods.
    def eat(animal: Animal): Unit // Abstract, instance method.
  }

  // Inheritance Model: A class can only extend up to one class, but inherit from (i.e., mixes in) multiple traits.
  class Crocodile extends Animal with Carnivore {
    override def eat(animal: Animal): Unit = println("Crunch")
  }

  // Singleton
  object MySingleton // singleton pattern in one line.

  // Companions: defining and object alongside the trait or class of the same name in a file. Allows for invocation of
  // methods using the dot notation on the trait/class name (i.e., static methods)
  object Carnivore { // Companion of trait Carnivore. Methods implemented here can be invoked by Carnivore.<method name>.
    def hunt(animal: Animal): Unit = println("Time to hunt.") // Class method, and must be implemented.
  }

  // Generics
  class MyList[T] { // Allows the reuse of this class for multiple class types
  }

  // Method notation
  val three = 1 + 2
  val anotherThree = 1.+(2) // These two are equivalent. All methods with a single symbol for a name can be used this
                            // way (i.e., infix notation).

  // Functional programming: functions can be treated like any other value (e.g., they can be passed around or
  // returned).
  val incrementer: Int => Int = x => x + 1  // Function accepts an Int type as an argument and returns an Int type.
                                            // Defined as an anonymous function.
  val incremented = incrementer(45) // 46

  // Higher-order functions: functions that can accept other functions (e.g., map, flatMap, filter).
  val processedList = List(1, 2, 3).map(incrementer) // List(2, 3, 4)
  val aLongerList = List(1, 2, 3).flatMap(x => List(x, x + 1))  // For each element, return a list and then the overall
                                                                // list is flattened (i.e., List(1, 2, 2, 3, 3, 4)).

  // for-comprehensions: available to all data structures that support map and flatMap.
  val checkerboard = List(1, 2, 3).flatMap(n => List('a', 'b', 'c').map(c => (n, c))) // A flattened List of the tuples
                                                                                      // of the Cartesian Product of the
                                                                                      // numbers and characters.
  val anotherCheckerboard = for {
    n <- List(1, 2, 3)
    c <- List('a', 'b', 'c')
  } yield (n, c) // equivalent expression

  // Options and Try
  val anOption: Option[Int] = Option(/* Something that might be null. */ 3) // Some(3)
  val doubledOption: Option[Int] = anOption.map(_ /* Short for x => x */ * 2) // Some(3 * 2)

  val anAttempt = Try(/* Something that might throw */ 42)  // Success(42) or Failure(Exception) if there is an
                                                            // exception.
  val aModifiedAttempt: Try[Int] = anAttempt.map(_ + 10)

  // Pattern matching
  val anUnknown: Any = 45
  val ordinal = anUnknown match {
    case 1 => "first"
    case 2 => "second"
    case _ => "unknown" // Catch-all case.
  }

  val optionDescription: String = anOption match {
    case Some(value) => s"the option is not empty: $value"
    case None => "the option is empty"
  }

  // Futures: needs an ExecutionContext (i.e., a data structure that holds the mechanism for scheduling threads).
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
  val aFuture = Future{ // or Future(42)
    // Some code.
    42
  }

  // wait for completion (async)
  aFuture.onComplete { // This receives a Try object.
    case Success(value) => println(s"The async meaning of life is $value")
    case Failure(exception) => println(s"Meaning of value failed: $exception")
  }

  // map a Future.
  val anotherFuture = aFuture.map(_ + 1) // Future(43) when it completes.

  // Partial functions: based on pattern matching.
  val aPartialFunction: PartialFunction[Int, Int] /* Accepts an argument that satisfies the specified patterns and returns an Int value */ = {
    case 1 => 43
    case 8 => 56
    case 100 => 999
  } // Only accepts values of the specified cases.

  // Some more advanced stuff.
  trait HigherKindedType[F[_] /* F has its own type arguments. */]
  trait SequenceChecker[F[_]] {
    def isSequential: Boolean
  }

  val listChecker = new SequenceChecker[List /* Not the absent additional type argument. */] {
    override def isSequential: Boolean = true
  }

  def main(args: Array[String]): Unit = {}
}
