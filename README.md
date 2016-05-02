# Slick 2.1.0 Problem: No matching Shape found #

This project illustrates the issue described in
[Slick 2.1.0: No matching Shape found for mapped type](http://stackoverflow.com/questions/36976156/slick-2-1-0-no-matching-shape-found-for-mapped-type)
on StackOverflow.

I've made progress since I found [activator-hello-slick](https://github.com/typesafehub/activator-hello-slick/blob/slick-2.1)
Still working things out:

````
[error] /var/work/experiments/slick/slick_2.1.0/src/main/scala/Problem.scala:32: Cannot perform option-mapped operation
[error]       with type: (String, Long) => R
[error]   for base type: (String, String) => Boolean
[error]     this.filter(p => p.provider === userId && p.userId === userId).firstOption
[error]                                 ^
[error] /var/work/experiments/slick/slick_2.1.0/src/main/scala/Problem.scala:32: ambiguous implicit values:
[error]  both value BooleanColumnCanBeQueryCondition in object CanBeQueryCondition of type => scala.slick.lifted.CanBeQueryCondition[scala.slick.lifted.Column[Boolean]]
[error]  and value BooleanOptionColumnCanBeQueryCondition in object CanBeQueryCondition of type => scala.slick.lifted.CanBeQueryCondition[scala.slick.lifted.Column[Option[Boolean]]]
[error]  match expected type scala.slick.lifted.CanBeQueryCondition[Nothing]
[error]     this.filter(p => p.provider === userId && p.userId === userId).firstOption
[error]                ^
````
