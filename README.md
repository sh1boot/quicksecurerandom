quicksecurerandom
=================
Since it [came up](http://stackoverflow.com/a/17749722/2417578) on
StackOverflow;

Apparently `java.security.SecureRandom.nextInt(int n)` defaults to the
`java.util.Random` implementation, which is computationally efficient but
wasteful.  Since `SecureRandom` bits are much higher cost, it doesn't perform
so well with such a greedy algorithm.

I tried putting the [Doctor Jacques method](http://mathforum.org/library/drmath/view/65653.html)
in there, but `.next()` turned out to be wasteful too (which was a bit of a
surprise!), so I modified it.

Now I'm going to leave it lying around with the intention of checking to make
sure it's correct, but that's never actually going to happen.  I'm no Java
programmer.

Original performance analysis:
[Bit recycling for scaling random number generators](http://arxiv.org/pdf/1012.4290.pdf)
