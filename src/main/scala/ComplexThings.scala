import slick.driver.PostgresDriver.simple._

class _ComplexThings(tag: Tag) extends Table[ComplexThing](tag, "complex_thing") {
  def one      = column[String]("one")
  def maybeInt = column[Option[Int]]("maybe_int") // specifying O.Nullable makes no difference
  def id       = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  // Blarg fields
  def x   = column[String]("x")
  def y   = column[Int]("y")

  // Blarg sub-projection mappings done the hard way:
  /*def blarg = (x, y).<>[Blarg, (String, Int)](
    { case tuple@(x, y) => Blarg.tupled(tuple) },
    { case blarg: Blarg => Blarg.unapply(blarg) }
  )*/

  // the easy way:
  def blarg = (x, y) <> (Blarg.tupled, Blarg.unapply)

  def * = (one, maybeInt, blarg, id) <> (ComplexThing.tupled, ComplexThing.unapply)
}

object ComplexThings extends TableQuery(new _ComplexThings(_)) {
  import DB._

  implicit val session = db.createSession

  def apply(one: String, two: Option[Int], blarg: Blarg, id: Option[Long]=None) = ComplexThing(one, two, blarg, id)

  def createTable(): Unit = this.ddl.create

  def deleteTable(): Unit = this.ddl.drop

  def findAll: List[ComplexThing] = ComplexThings.list

  def findById(id: Option[Long]): Option[ComplexThing] = ComplexThings.filter(_.id === id).firstOption

  def findById(id: Long): Option[ComplexThing] = queryById(id).firstOption

  /** Compares Option[Int] with Column[Option[Int]] and does the right thing */
  def findByMaybeInt(maybeInt: Option[Int]): List[ComplexThing] =
    ComplexThings.filter(_.maybeInt === maybeInt).list

  /** Compares Int with Column[Option[Int]] and does the right thing */
  def findByMaybeInt2(int: Int): List[ComplexThing] =
    ComplexThings.filter(_.maybeInt === int).list

  protected def queryById(id: Long): Query[_ComplexThings, ComplexThing, Seq] = ComplexThings.filter(_.id === id)

  def insert(oauthProvider: ComplexThing): Int = this += oauthProvider
}
