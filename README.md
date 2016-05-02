# Slick 2.1.0 Problem #

This project illustrates the issue described in
[Slick 2.1.0: No matching Shape found for mapped type](http://stackoverflow.com/questions/36976156/slick-2-1-0-no-matching-shape-found-for-mapped-type)
on StackOverflow.

````
Error:(13, 11) No matching Shape found.
Slick does not know how to map the given types.
Possible causes: T in Table[T] does not match your * projection. Or you use an unsupported type in a Query (e.g. scala List).
  Required level: scala.slick.lifted.FlatShapeLevel
     Source type: (scala.slick.lifted.Column[String], scala.slick.lifted.Column[String], scala.slick.lifted.Column[String], scala.slick.lifted.Column[Option[Long]])
   Unpacked type: OAuthProvider
     Packed type: Any
  ).shaped[OAuthProvider]
          ^
````

I tried another approach using [activator-hello-slick](https://github.com/typesafehub/activator-hello-slick/blob/slick-2.1).
Unfortunately the example deals in tuples, not instances of the persisted classes, and it returns this error:

````
[error] /var/work/experiments/slick/slick_2.1.0/src/main/scala/Problem.scala:17: overriding method * in class AbstractTable of type => scala.slick.lifted.ProvenShape[OAuthProvider];
[error]  method * has incompatible type
[error]   def * : ProvenShape[(String, String, String, Option[Long])] = (email, provider, userId, id)
[error]       ^
               ^
````
