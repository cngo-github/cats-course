package part1intro

object TCVariance {
  import cats.Eq
  import cats.instances.int._
  import cats.instances.option._
  import cats.syntax.eq._

  val aComparison = Option(2) === Option(3)
  // val anInvalidComparison = Some(2) === None // Eq[Some[Int]] not found.

  // variance
  class Animal
  class Cat extends Animal

  // Covariant type: allows subtype to be propagated to the generic type (i.e., polymorphism)
  class Cage[+T]
  val cage: Cage[Animal] = new Cage[Cat] // Cat <: (is subclass of or extends) Animal ==> Cage[Cat] <: Cage[Animal]

  // Contravariant type: propagates subtype backwards. Typically an action type, it acts on the types.
  class Vet[-T]
  val vet: Vet[Cat] = new Vet[Animal] // Cat <: Animal ==> Vet[Animal] <: Vet[Cat]
  // Intuition: You want a Vet for Cats, at runtime, I give you a vet for all animals (i.e., a better vet). So, the vet
  // I give you can also heal your Cat.

  // Rule of thumb: "Has a T" = covariant, "Acts on T" = contravariant
  // Variance affect how TC instances are being fetched

  trait SoundMaker[-T]
  implicit object AnimalSoundMaker extends  SoundMaker[Animal]
  def makeSound[T](implicit  soundMaker: SoundMaker[T]): Unit = println("Wow")
  makeSound[Animal] // Ok - TC defined above.
  makeSound[Cat] // OK - TC contravariance. TC instance for Animal is also applicable to Cat.

  // Rule 1: Contravariant TCs can use the superclass instances if nothing is available strictly for that type.

  implicit object OptionSoundMaker extends SoundMaker[Option[Int]]
  makeSound[Option[Int]]
  makeSound[Some[Int]]

  // Covariant TC
  trait AnimalShow[+T] {
    def show: String
  }
  implicit object GeneralAnimalShow extends AnimalShow[Animal] {
    override def show: String = "An animal is everywhere!"
  }
  implicit object CatsShow extends AnimalShow[Cat] {
    override def show: String = "A cat is everywhere!"
  }
  def organizeShow[T](implicit event: AnimalShow[T]): String = event.show
  // Rule 2: Covariant TCs will always use the more specific TC instance for that type, but may confuse the compiler if
  // the general TC is also present.

  // Rule 3: you can't have both benefits.

  // Cats uses invariant TCs.
  Option(2) === Option.empty[Int] // Compare an Option with a value to None. Use the general type and use smart
                                  // constructors to convert to more specific types.

  def main(args: Array[String]): Unit = {
    println(organizeShow[Cat]) // Ok - the compiler will inject CatsShow
    // println(organizeShow[Animal]) -- Error - ambiguous value - the compiler finds AnimalShow and CatsShow, is
    // unsure which to inject.
  }
}
