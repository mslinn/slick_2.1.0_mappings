package model

import slick.driver.PostgresDriver.simple._

case class OAuthProvider(
  email: String,
  provider: String,
  userId: String,
  id: Option[Long]=None
)

class OAuthProviders(tag: Tag) extends Table[OAuthProvider](tag, "oauthProvider") {
  def email    = column[String]("email")
  def provider = column[String]("provider")
  def userId   = column[String]("userid")
  def id       = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

  private type OAuthProviderTupleType = (String, String, String, Option[Long])

  private val oauthProviderShapedValue = (
    email, provider, userId, id
  ).shaped[OAuthProvider]

  /** Above gives error: Slick does not know how to map the given types.
  [error] Possible causes: T in Table[T] does not match your * projection. Or you use an unsupported type in a Query (e.g. scala List).
  [error]   Required level: scala.slick.lifted.FlatShapeLevel
  [error]      Source type: (scala.slick.lifted.Column[String], scala.slick.lifted.Column[String], scala.slick.lifted.Column[String], scala.slick.lifted.Column[Option[Long]])
  [error]    Unpacked type: model.OAuthProvider
  [error]      Packed type: Any
  [error]   ).shaped[OAuthProvider] */

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

  def * = oauthProviderShapedValue <> (toModel, toTuple)
}

object oAuthProviders extends TableQuery(new OAuthProviders(_)) {
  val Logger = org.slf4j.LoggerFactory.getLogger("db")

  def createTable(): Unit = this.ddl.create

  def apply(email: String, provider: String, userId: String, id: Option[Long]=None) =
    OAuthProvider(email, provider, userId, id)

  lazy val queryAll = Query(oAuthProviders)

  def findByUserId(userId: IdentityId): Option[OAuthProvider] =
    this.filter(p => p.provider === userId.providerId && p.userId === userId.userId).firstOption

  def findByEmailAndProvider(email: String, providerId: String): Option[OAuthProvider] =
    this.filter(p => p.provider === providerId && p.email === email).firstOption

  def findByEmail(email: String): Option[OAuthProvider] = this.filter(_.email === email).firstOption

  protected def queryById(id: Long): Query[OAuthProviders, OAuthProvider, Seq] = filter(_.id === id)

  def findById(id: Long): Option[OAuthProvider] = queryById(id).firstOption

  def upsert(newOAP: OAuthProvider): Long = {
    def updateOAP(oap: OAuthProvider): Long = {
      queryById(newOAP.id.get).update(oap)
      oap.id.get
    }

    if (newOAP.id.isDefined && newOAP.id.get>0) { // update existing OAuthProvider
      findById(newOAP.id.get) match {
        case Some(_) =>
          updateOAP(newOAP)

        case None => // something is wrong; id is set but the OAuthProvider is not found in the database
          Logger.warn(s"Section with id=${ newOAP.id } is not in the database. Persisting but this is probably an error you want to look into.")
          this += newOAP
      }
    } else { // look for OAuthProvider again before inserting new instance
      findByEmailAndProvider(newOAP.email, newOAP.provider) match { // Make new OAuthProvider if it does not already exist
        case Some(oap) =>
          updateOAP(newOAP.copy(id=oap.id))

        case None =>
          this += newOAP
      }
    }
  }
}

