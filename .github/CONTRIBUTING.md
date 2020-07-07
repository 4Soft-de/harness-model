# Contributing

## Git setup

1. Fork this repository, clone the fork and navigate into it.
2. Add an upstream to the original repository to keep a reference to it.<br />
   `git remote add upstream https://github.com/4Soft-de/jaxb-enhanced-navigation`
3. Checkout a new branch.<br />
   `git checkout -b feature/your_feature`
4. Business as usual, commit and push to your own repository.
5. When done, go to either your repository or to our repository and create a Pull Request.
6. Pay attention to our [Pull Request Template](https://github.com/4Soft-de/jaxb-enhanced-navigation/blob/develop/.github/PULL_REQUEST_TEMPLATE.md).
7. Allow edits from maintainers.
8. Submit the pull request.

### Keep your fork in sync

If you want to contribute again, you need to update your fork to our current state.

1. Fetch all branches from the upstream.<br />
   `git fetch upstream`
2. Rewrite your develop's history with upstream’s develop.<br />
   `git rebase upstream/develop`
3. (Force) push your develop to the remote state.<br />
   `git push origin develop --force`

## IDE setup

### IntelliJ
1. `File -> New -> Project from Existing Sources...`
2. Navigate to the cloned project and choose it.
3. `Import project from external model` -> Select `Maven`

### Eclipse
1. `File -> Import`
2. Search for `Maven` -> Select `Existing Maven Projects`
3. Navigate to the cloned project and choose it.
4. Select the POM.xml of the project (in this case select the main or all pom.xml's).
5. Confirm by clicking `Finish`.

### Visual Studio Code
1. `File -> Open Folder`
2. Navigate to the cloned project and choose it.

## Making changes

No matter if you do code or documentation changes, the changes have to be consistent. We didn't use special save actions
or code formatter, so a vanilla installation of your IDE is fine for it. If you do have them, make sure they
don't break the format, so the diffs only contain the real changes.

#### Rules in general
- Put curly braces on the same line as the code is, don't put them on a new line (as you would do in C#).
- Use an indentation of 4 spaces instead of tabs.
- Variables should be final wherever possible.
- Follow code conventions when writing code.
- Only use the lowest visibility modifier (public, protected, package, private) possible. 
- JavaDoc for public method is required, for the rest its optional.

#### Example
##### Bad
```java
public class BadCode
{
   public String GetFirstWord(String x)
   {
     boolean contains_space = containsspace(x);
     if(contains_space){
         return x.substring(0, x.indexOf(" ")+1);
     }else{
         return x;
     }
   }
   public boolean containsspace(String x){
      return x.contains(" ");
   }
}
```
##### Good
```java
public class BetterCode {

    /**
     * Gets the first word from a given string.
     *
     * @param input Input string to get the first word from
     * @return The first word if the string contains a space or the input else.
     */
    public String getFirstWord(final String input) {
        if (input == null) {
            return null;
        }
        final boolean containsSpace = containsSpace(input);  // not really needed, just for modifier
        if (containsSpace) {
            return input.substring(0, input.indexOf(' ') + 1);
        } else {
            return input;
        }
    }

    private boolean containsSpace(final String input) {
        return input.contains(" ");
    }
}
```