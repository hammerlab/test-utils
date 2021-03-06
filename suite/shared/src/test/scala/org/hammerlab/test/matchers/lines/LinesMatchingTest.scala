package org.hammerlab.test.matchers.lines

class LinesMatchingTest
  extends hammerlab.Suite
    with HasLines {

  test("plain strings") {
    "abc def\tghi" should linesMatch("abc def\tghi")
    "" should linesMatch("")

    "" should not(linesMatch("a"))
    "a" should not(linesMatch("b"))
    "a" should not(linesMatch(""))
  }

  test("multiple lines") {
    """abc def	ghi
      |jkl
      |
      |m
      |
      |"""
      .stripMargin should linesMatch(
      "abc def\tghi",
      "jkl",
      "",
      "m",
      "",
      ""
    )

    """abc def	ghi
      |jkl
      |
      |
      |m
      |"""
    .stripMargin should linesMatch(
      "abc def\tghi",
      "jkl",
      "",
      "",
      "m",
      ""
    )
  }

  test("wildcards") {
    """abc 1
      |123 def
      |ggg45hhh
      |77 ,,123  88
      |"""
    .stripMargin should linesMatch(
      l"abc $ln",
      l"$ln def",
      l"ggg${ln}hhh",
      l"$ln${Chars(",123 ")}$ln",
      ""
    )

    """abc 1
      |123 def
      |ggg45hhh
      |77 ,,1243  88
      |"""
    .stripMargin should not(
      linesMatch(
        l"abc $ln",
        l"$ln def",
        l"ggg${ln}hhh",
        l"$ln${Chars(",123 ")}$ln",
        ""
      )
    )
  }

  test("not-chars") {
    "abc" should linesMatch(NotChars("def"))
    "abc" should not(linesMatch(NotChars("cde")))
    "" should not(linesMatch(NotChars("def")))

    "abc" should linesMatch(NotChar('d'))
    "abdc" should not(linesMatch(NotChar('d')))
  }

  test("prefix") {
    """abc 1
      |123 def
      |ggg45hhh
      |77 ,,123  88
      |"""
    .stripMargin should firstLinesMatch(
      l"abc $ln",
      l"$ln def",
      l"ggg${ln}hhh"
    )
  }
}
