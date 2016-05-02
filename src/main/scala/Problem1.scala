import scala.slick.lifted.ShapedValue
import slick.driver.PostgresDriver.simple._

class OAuthProviders1(tag: Tag) extends Table[OAuthProvider](tag, "oauthProvider") {
  def email    : Column[String] = column[String]("email")
  def provider : Column[String] = column[String]("provider")
  def userId   : Column[String] = column[String]("userid")
  def id       : Column[Option[Long]] = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  private type OAuthProviderTupleType = (String, String, String, Option[Long])

  private val toModel: OAuthProviderTupleType => OAuthProvider = { tuple =>
    OAuthProvider(
      email    = tuple._1,
      provider = tuple._2,
      userId   = tuple._3,
      id       = tuple._4
  )}

  private val toTuple: OAuthProvider => Option[OAuthProviderTupleType] = { oap =>
    Some((
      oap.email,
      oap.provider,
      oap.userId,
      oap.id
    ))
  }

  def * = (
    email, provider, userId, id
  ) <> (toModel, toTuple)
}

object oAuthProviders1 extends TableQuery(new OAuthProviders1(_)) {
  val Logger = org.slf4j.LoggerFactory.getLogger("db")
  val db = Database.forURL("jdbc:postgresql:localhost:5432/slinnbooks", driver = "org.postgresql.Driver")
  implicit val session = db.createSession

  def createTable(): Unit = this.ddl.create

  def apply(email: String, provider: String, userId: String, id: Option[Long]=None) =
    OAuthProvider(email, provider, userId, id)

  def findByUserId(userId: String): Option[OAuthProvider] =
    this.filter(p => p.userId === userId).firstOption

  protected def queryById(id: Long): Query[OAuthProviders1, OAuthProvider, Seq] = filter(_.id === id)

  def findById(id: Long): Option[OAuthProvider] = queryById(id).firstOption

  def insert(oauthProvider: OAuthProvider): Int = {
    oAuthProviders1 += OAuthProvider("x@y.com", "blah", "blah")
  }
}

object app1 extends App {
  import oAuthProviders1.Logger

  oAuthProviders1.createTable()
  val id: Int = oAuthProviders1.insert(OAuthProvider("x@y.com", "blah", "blah"))
  oAuthProviders1.findById(id).map { oAuthProvider =>
    Logger.info(s"Found $oAuthProvider")
    oAuthProvider
  }.orElse {
    Logger.info("Did not find the OAuthProvider")
    None
  }
}
