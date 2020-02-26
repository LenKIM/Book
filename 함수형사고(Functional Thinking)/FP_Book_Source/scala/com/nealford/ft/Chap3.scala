object Chap3 extends App {

  //// 3-11
  def price(product : String) : Double =
    product match {
      case "apples" => 140
      case "oranges" => 223
  }

  def withTax(cost: Double, state: String) : Double =
    state match {
      case "NY" => cost * 2
      case "FL" => cost * 3
  }

  val locallyTaxed = withTax(_: Double, "NY")
  val costOfApples = locallyTaxed(price("apples"))

  assert(Math.round(costOfApples) == 280)


  //// 3-12
  val cities = Map("Atlanta" -> "GA", "New York" -> "New York",
  "Chicago" -> "IL", "San Francsico " -> "CA", "Dallas" -> "TX")

  cities map { case (k, v) => println(k + " -> " + v) }


  //// 3-13
  //List(1, 3, 5, "seven") map { case i: Int => i + 1 }  // won't work
  // scala.MatchError: seven (of class java.lang.String)

  List(1, 3, 5, "seven") collect { case i: Int => i + 1 }
  // verify
  assert(List(2, 4, 6) == (List(1, 3, 5, "seven") collect { case i: Int => i + 1 }))


  //// 3-14
  val answerUnits = new PartialFunction[Int, Int] {
      def apply(d: Int) = 42 / d
      def isDefinedAt(d: Int) = d != 0
  }

  assert(answerUnits.isDefinedAt(42))
  assert(! answerUnits.isDefinedAt(0))
  assert(answerUnits(42) == 1)
  //answerUnits(0)
  //java.lang.ArithmeticException: / by zero


  //// 3-15
  def pAnswerUnits: PartialFunction[Int, Int] =
      { case d: Int if d != 0 => 42 / d }

  assert(pAnswerUnits(42) == 1)
  //pAnswerUnits(0)
  //scala.MatchError: 0 (of class java.lang.Integer)


  //// 3-16
  def inc: PartialFunction[Any, Int] =
    { case i: Int => i + 1 }

  assert(inc(41) == 42)
  //inc("Forty-one")
  //scala.MatchError: Forty-one (of class java.lang.String)

  assert(inc.isDefinedAt(41))
  assert(! inc.isDefinedAt("Forty-one"))

  assert(List(42) == (List(41, "cat") collect inc))

}
