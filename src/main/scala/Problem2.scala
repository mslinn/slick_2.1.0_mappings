import slick.driver.PostgresDriver.simple._
import slick.lifted.ProvenShape

class OAuthProviders2(tag: Tag) extends Table[OAuthProvider](tag, "oauthProvider") {
  def email    : Column[String] = column[String]("email")
  def provider : Column[String] = column[String]("provider")
  def userId   : Column[String] = column[String]("userid")
  def id       : Column[Option[Long]] = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  def * : ProvenShape[(String, String, String, Option[Long])] = (email, provider, userId, id)
}

object oAuthProviders2 extends TableQuery(new OAuthProviders2(_)) {
  val Logger = org.slf4j.LoggerFactory.getLogger("db")
  val db = Database.forURL("jdbc:postgresql:localhost:5432/slinnbooks", driver = "org.postgresql.Driver")
  implicit val session = db.createSession

  def createTable(): Unit = this.ddl.create

  def apply(email: String, provider: String, userId: String, id: Option[Long]=None) =
    OAuthProvider(email, provider, userId, id)

  def findByUserId(userId: String): Option[OAuthProvider] =
    this.filter(p => p.userId === userId).firstOption

  protected def queryById(id: Long): Query[OAuthProviders2, OAuthProvider, Seq] = filter(_.id === id)

  def findById(id: Long): Option[OAuthProvider] = queryById(id).firstOption

  def insert(oauthProvider: OAuthProvider): Int = {
    oAuthProviders2 += OAuthProvider("x@y.com", "blah", "blah")
  }
}

object app2 extends App {
  import oAuthProviders2.Logger

  oAuthProviders2.createTable()
  val id: Int = oAuthProviders2.insert(OAuthProvider("x@y.com", "blah", "blah"))
  oAuthProviders2.findById(id).map { oAuthProvider =>
    Logger.info(s"Found $oAuthProvider")
    oAuthProvider
  }.orElse {
    Logger.info("Did not find the OAuthProvider")
    None
  }
}
