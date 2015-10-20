package io.github.finaglecircuit.util

import com.twitter.util.{Future => TwitterF, Promise => PromiseF}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future => ScalaF, Promise => ScalaP}
import scala.language.implicitConversions

object FutureConversions {
  implicit def asScala[T](twitterF: TwitterF[T]): ScalaF[T] = {
    val scalaP = ScalaP[T]()
    twitterF.onSuccess { r: T =>
      scalaP.success(r)
    }
    twitterF.onFailure { e: Throwable =>
      scalaP.failure(e)
    }
    scalaP.future
  }

  implicit def asTwitter[T](scalaF: ScalaF[T]): TwitterF[T] = {
    val tP = PromiseF[T]()
    scalaF.onSuccess { case r: T => tP.setValue(r)}
    scalaF.onFailure { case e: Throwable => tP.setException(e)}
    tP
  }
}
