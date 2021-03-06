package org.hammerlab.test.matchers.seqs

import org.scalatest.matchers.{ MatchResult, Matcher }

import scala.collection.mutable.ArrayBuffer

case class SeqMatcher[T](expected: Iterable[T],
                         matchOrder: Boolean = true)
  extends Matcher[Iterable[T]] {

  sealed trait Error

  case class Line(line: String) extends Error
  implicit def makeLine(line: String): Line = Line(line)

  case class Elems(elems: Iterable[T], _indent: String = "\t") extends Error {
    def indent(str: String): Elems = copy(_indent = str)
  }
  implicit def makeElems(elems: Iterable[T]): Elems = Elems(elems)

  def err(errs: Error*)(implicit errors: ArrayBuffer[String]): Unit =
    errs foreach {
      case Line(str) ⇒
        errors += str
      case Elems(elems, indent) ⇒
        for {elem ← elems} {
          errors += s"$indent$elem"
        }
        errors += ""
    }

  override def apply(actual: Iterable[T]): MatchResult = {
    val expectedSet: Set[T] = expected.toSet

    val actualSet: Set[T] = actual.toSet

    implicit val errors = ArrayBuffer[String]()

    err(
      "Sequences didn't match!",
      ""
    )

    val extraElems = actualSet.diff(expectedSet)
    val missingElems = expectedSet.diff(actualSet)

    if (extraElems.nonEmpty || missingElems.nonEmpty) {

      if (extraElems.nonEmpty)
        err(
          "Extra elems:",
          extraElems
        )

      if (missingElems.nonEmpty)
        err(
          "Missing elems:",
          missingElems
        )

    } else if (matchOrder && !actual.sameElements(expected))
      err(
        "Elements out of order:",
        "",
        "Expected:",
        expected,
        "Actual:",
        actual
      )


    val matched =
      if (matchOrder)
        actual.sameElements(expected)
      else
        actualSet == expectedSet

    MatchResult(
      matched,
      errors.mkString("\n"),
      s"$actual matched; was supposed to not."
    )
  }
}

trait HasSeqMatcher {
  def seqMatch[T](expected: Iterable[T]): Matcher[Iterable[T]] = SeqMatcher[T](expected.toSeq)
  def seqMatch[T](expected: Iterator[T]): Matcher[Iterable[T]] = SeqMatcher[T](expected.toSeq)
}
