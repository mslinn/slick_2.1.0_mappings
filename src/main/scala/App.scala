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

  SimpleThings.createTable()
  val simpleThingId: Int = SimpleThings.insert(SimpleThing("x@y.com", "blah", "blah", InnerDoodle("doodley-doo!")))
  val simpleThing: Option[SimpleThing] = SimpleThings.findById(simpleThingId)
  Logger.info(s"simpleThing=$simpleThing")

  ComplexThings.createTable()
  val complexThingId: Int = ComplexThings.insert(ComplexThing("and a one", 22, Blarg("Blarg", 99)))
  val complexThing: Option[ComplexThing] = ComplexThings.findById(complexThingId)
  Logger.info(s"complexThing=$complexThing")

  SimpleThings.deleteTable()
  ComplexThings.deleteTable()
}
