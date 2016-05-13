import scala.slick.jdbc.StaticQuery

object DB {
  val Logger = org.slf4j.LoggerFactory.getLogger("db")

  def db = {
    import slick.jdbc.{StaticQuery => Q}
    import slick.driver.JdbcDriver.backend.Database

    val pgUser: String = sys.env("USERID")
    val pgPassword: String = sys.env("PGPASSWORD")
    val dbName = "oauthTest"
    val database = Database.forURL(s"jdbc:postgresql://localhost:5432/postgres", driver="org.postgresql.Driver", user=pgUser, password=pgPassword)
    database.withSession { implicit session =>
      Q.updateNA(s"drop database if exists $dbName; create database $dbName;").execute
    }
    database
  }
}

object app extends App {
  import DB.Logger

  try {          SimpleThings.deleteTable()  } catch { case _: Exception => }
  try {         ComplexThings.deleteTable() } catch { case _: Exception => }
  try { OptionalComplexThings.deleteTable() } catch { case _: Exception => }

  val blarg0 = Blarg("Blarg", 0)
  val blarg42 = Blarg("Blarg", 42)

  SimpleThings.createTable()
  val simpleThingId: Int = SimpleThings.insert(SimpleThing("x@y.com", "blah", "blah", InnerDoodle("doodley-doo!")))
  val simpleThing: Option[SimpleThing] = SimpleThings.findById(simpleThingId)
  Logger.info(s"simpleThing=$simpleThing")

  Logger.info("")

  ComplexThings.createTable()
  val complexThingId1: Int = ComplexThings.insert(ComplexThing("Stores null in maybe_int column", None, blarg0))
  val complexThingId2: Int = ComplexThings.insert(ComplexThing("Stores 42 in maybe_int column", Some(42), blarg42))
  DB.db.withSession { implicit session =>
    import slick.jdbc.{StaticQuery => Q}
    Q.updateNA(s"""insert into complex_thing (one, x, y) values ('Stores null in maybe_int column', 'x', 1);""").execute
  }

  Logger.info(s"ComplexThings.findAll=${ ComplexThings.findAll.mkString("\n  ", "\n  ", "\n") }")
  Logger.info(s"ComplexThings.findById($complexThingId1)=${ ComplexThings.findById(complexThingId1) }")
  Logger.info(s"ComplexThings.findByMaybeInt(Some(42)=${ ComplexThings.findByMaybeInt(Some(42)).mkString(", ") }")
  Logger.info(s"ComplexThings.findByMaybeInt(Some(0) should not return results " + ComplexThings.findByMaybeInt(Some(0)).mkString(", "))
  Logger.info("ComplexThings.findByMaybeInt(None) does not return results which is usually what you want " +
              ComplexThings.findByMaybeInt(None).mkString)
  Logger.info(s"ComplexThings.findByMaybeInt2(42)=${ ComplexThings.findByMaybeInt2(42).mkString(", ") }")
  Logger.info(s"ComplexThings.findByMaybeInt2(0) should not return results " + ComplexThings.findByMaybeInt2(0).mkString(", "))
  Logger.info(s"ComplexThings.findById($complexThingId2)=${ ComplexThings.findById(complexThingId2) }")


  OptionalComplexThings.createTable()
  val optionalComplexThingId1: Int = OptionalComplexThings.insert(OptionalComplexThing("blarg0", None, None))
  val optionalComplexThingId2: Int = OptionalComplexThings.insert(OptionalComplexThing("blarg42", Some(42), Some(blarg42)))


  SimpleThings.deleteTable()
  ComplexThings.deleteTable()
  OptionalComplexThings.deleteTable()
}
