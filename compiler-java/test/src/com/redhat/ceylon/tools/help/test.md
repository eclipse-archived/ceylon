# Test

## What is this document for?

This document is a simple test case for how we might convert markdown 
formatted documents to plain text. It also serves as a description of how we 
plan to do this.


### Why?

You might ask why? Markdown documents are relatively close to plain text any 
way. You're right, they are but there's still not quite the same:

* Markdown documents exist to be formatted by a markdown processor, but true 
plain text documents do not. 

* Markdown documents *may* be hard line-wrapped, but may not be. Plain text
documents should *always* be line wrapped. The default is 80 columns.

Where markdown permits alternative formatting, the plain 
text output should canonicalize this to a single preferred form. 


## Block level formatting

### Paragrahps

Paragraphs are always followed by a *single* empty line. In particular this 
means that consecutive paragraphs are separated by a *single* empty line.


### Headers

Markdown permits two formats which are processed to an HTML `<h1>`:

    # Atx-style headers
    
and

    Setext-style H1 headers
    =======================

The plaintext format is the Setext-style.

Simiarly for `<h2>`, markdown permits:

    ## Atx-style headers
    
and

    #### Setext-style H2 headers ####
    ---------------------------------

The plaintext format is again Setext-style.

For `<h3> to `<h6>` we use the following

    #### H3 example ####
    
    This is an example of a H3 heading.
    
    
    ### H4 example ###
    
    This is an example of a H4 heading.
    
    
    ## H5 example ##
    
    This is an example of a H5 heading.
    
    
    # H6 example #
    
    This is an example of a H6 heading.

This is *not* the same as Atx-style, because the number of `#` *decreases* with 
the importance of the heading, so that the visual impact of more important 
headers is preserved.

All headers are preceded by an empty line (unless the header is the first 
construct in the document). Because paragraph constructs are followed by an 
empty line this helps make the structure stand out.


### Blockquotes

The plaintext format is the same as the markdown format, only we canonicalize 
as follows:

    > Markdown permits blockquotes
    where only the first line begins with a `>`
    
is transformed to:

    > Markdown permits blockquotes
    > where only the first line begins with a `>`


#### Aside

By prefixing every line with a `> ` we make lines longer, thus more prone 
to line wrapping. But line wrapping is allowed in blockquotes anyway.

        
#### Aside

**TODO** What about line wrapping?


### Unordered lists

* Plain text output always uses the `*` for unordered lists. 
* A handing indent is applied. 
* Items are separated by an empty line.


### Ordered lists

1. We always use numbers, but if they're not in sequence (allowed by markdown)
then they're put in sequence.

1. Otherwise the same rules for unordered lists apply.


### Code

Some markdown processor support 'fenced code blocks', for example:

    ```
    Example of a fenced code block
    ```
    
These are converted to normal markdown format, using spaces:

        Example of a fenced code block


### Horizontal rules:

These are canonically represented by `* * *` centered, like this:

                               * * *


## Inline formatting

### Links

Inline links such as

    [this example(http://example.com)
    
should be transformed to something like:

    this example (http://example.com)

Reference-style links such as:

    [this example][blah]
   
use the same format, i.e.:

    this example (http://example.com)


### Strong

What markdown considers to be strong text is transformed to uppercase. For 
example

    **This** and __this__
    
would be transformed to:

    THIS and THIS


### Emphasis

What markdown considers to be emphasized text is enclosed with `*`. For 
example

    *This* and _this_
    
would be transformed to:

    *This* and *this*


### Inline code

Markdown supports inline code like `printf()`. In plaintext this is output as-is, 
including the backquotes. For example:


    My favourite thing about `printf()` is...
    
would be transformed to:

    My favourite thing about `printf()` is...


## Escaping

**Need a way to escape a newline, so that we generate a hard newline**
