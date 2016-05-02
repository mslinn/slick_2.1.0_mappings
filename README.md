# Slick 2.1.0 Problem: No matching Shape found #

This project illustrates the issue described in 
[Slick 2.1.0: No matching Shape found for mapped type](http://stackoverflow.com/questions/36976156/slick-2-1-0-no-matching-shape-found-for-mapped-type) 
on StackOverflow.

Here is the error message I'm trying to figure out:

````
[error] /var/work/experiments/slick/slick_2.1.0/src/main/scala/Problem.scala:22: No matching Shape found.
[error] Slick does not know how to map the given types.
[error] Possible causes: T in Table[T] does not match your * projection. Or you use an unsupported type in a Query (e.g. scala List).
[error]   Required level: scala.slick.lifted.FlatShapeLevel
[error]      Source type: (scala.slick.lifted.Column[String], scala.slick.lifted.Column[String], scala.slick.lifted.Column[String], scala.slick.lifted.Column[Option[Long]])
[error]    Unpacked type: model.OAuthProvider
[error]      Packed type: Any
[error]   ).shaped[OAuthProvider]
[error]           ^
````

