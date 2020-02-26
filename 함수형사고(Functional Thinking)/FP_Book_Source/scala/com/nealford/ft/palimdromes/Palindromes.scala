def isPalindrome(x: String) = x == x.reverse
def findPalindrome(s: Seq[String]) = s find isPalindrome

val sentence = "Bobby went to Harrah and gambled with Otto and Steve"
val words = sentence.toLowerCase().split(" ")
println(findPalindrome(words take 1))  // None
println(findPalindrome(words take 4))  // Some(harrah)
