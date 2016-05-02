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

  simpleThings.createTable()
  val simpleThingId: Int = simpleThings.insert(SimpleThing("x@y.com", "blah", "blah", InnerDoodle("doodley-doo!")))
  val simpleThing: Option[SimpleThing] = simpleThings.findById(simpleThingId)
  Logger.info(s"simpleThing=$simpleThing")

  complexThings.createTable()
  val complexThingId: Int = complexThings.insert(ComplexThing("and a one", 22, Blarg("Blarg", 99)))
  val complexThing: Option[ComplexThing] = complexThings.findById(complexThingId)
  Logger.info(s"complexThing=$complexThing")

  simpleThings.deleteTable()
  complexThings.deleteTable()
}
