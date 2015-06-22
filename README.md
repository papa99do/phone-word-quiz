# Reason to select this quiz
I am working on ACMA numbering project which dealing with the registering of the phone numbers including 1800 numbers.
This quiz seems to be a good extension to the features that helps smartnumber clients to find the numbers they want.

# General design consideration
- `NumberEncoding` interface and the default implementation encapsulates the logic to map from a letter to a digit and
 vise versa.
- `PhoneWordNode` encapsulates the core algorithm to build a trie-like tree to store the mapping information and by
 traversing this tree, you can get the mapping results for a given number
- `Dictionary` wraps the tree of `PhoneWordNodes`, cleans both of the input words and numbers, and generates the matching
 result as list of strings.
- `PhoneWordQuiz` is the main class, which drives the whole process. While `PhoneWordQuizOptions` facilitates this process
 by extracting the arguments from command line.

# Matching algorithm
PhoneWordNode represents a trie-like tree structure to store the encoded words. Each node has up to 10 children mapped
to number 0-9, and a list of words mapped all the way down to this level. The diagram below illustrates the design
intention better.

Say we have three words, and mapped to number based on encoding as following:
- ABD (223)
- AB (22)
- E (3)

The generated tree will be like
<pre>
   root -> 2 -> 2 [AB] -> 3 [ABD]
        -> 3 [E]
</pre>

When searching a match for a number, say 223, it start from the root node and traverse the tree according to the
individual digit. If a match found, and all digits of the number has been consumed, the words in the current node
will be added to the final result. If it's in the middle of a number, it will try to find a new word from that
position and join the result with the previously found word. The feature to skip a digit is supported by simply
allow skipping a digit when doing this recursive process.

# How to run
## Dependencies
- java 8

## Instructions
```
java -jar aconex-1800-quiz.jar [-d dictionary_file] [-f phone_number_file]
```