# Slick 2.1.0 Problem #

This project illustrates the issue described in
[Slick 2.1.0: No matching Shape found for mapped type](http://stackoverflow.com/questions/36976156/slick-2-1-0-no-matching-shape-found-for-mapped-type)
on StackOverflow.

I've made progress since I found [activator-hello-slick](https://github.com/typesafehub/activator-hello-slick/blob/slick-2.1)
Still working things out:

````
[error] /var/work/experiments/slick/slick_2.1.0/src/main/scala/Problem.scala:17: overriding method * in class AbstractTable of type => scala.slick.lifted.ProvenShape[OAuthProvider];
[error]  method * has incompatible type
[error]   def * : ProvenShape[(String, String, String, Option[Long])] = (email, provider, userId, id)
[error]       ^
               ^
````
