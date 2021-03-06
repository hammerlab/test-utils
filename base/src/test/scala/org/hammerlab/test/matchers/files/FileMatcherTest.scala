package org.hammerlab.test.matchers.files

import org.hammerlab.test.Suite
import org.hammerlab.test.resources.{File, stringToTestPath}

class FileMatcherTest
  extends Suite {

  test("equal files") {
    File("a/numbers").path should fileMatch("c/numbers")
    File("a/numbers").path should fileMatch(File("c/numbers"))
    File("a/numbers").path should fileMatch(File("c/numbers").path)
  }

  test("differing files") {
    val result = fileMatch("c/numbers")("b/numbers")
    ==(result.matches, false)
    ==(
      result.failureMessage,
      """"1
        |2
        |3
        |[4
        |5
        |6
        |]" was not equal to "1
        |2
        |3
        |[]""""
      .stripMargin
    )
  }

  test("error on directories") {
    val dir = tmpDir()
    var result = new FileMatcher(dir).apply("a/numbers")
    ==(result.matches, false)
    ==(result.failureMessage, s"'Expected' file $dir should not be a directory")

    result = fileMatch("a/numbers").apply(dir)
    ==(result.matches, false)
    ==(result.failureMessage, s"'Actual' file $dir should not be a directory")
  }
}
