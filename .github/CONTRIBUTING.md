# Contributing

## Setup yourself

1. Fork this repository, clone the fork and navigate into it.
2. Add an upstream to the original repository to keep a reference to it.<br />
   `git remote add upstream https://github.com/4Soft-de/vec-model`
3. Checkout a new branch.<br />
   `git checkout -b feature/your_feature`
4. Business as usual, commit and push to your own repository.
5. When done, go to either your repository or to our repository and create a Pull Request.
6. Pay attention to our [Pull Request Template](https://github.com/4Soft-de/vec-model/blob/develop/.github/PULL_REQUEST_TEMPLATE.md).
7. Allow edits from maintainers.
8. Submit the pull request.

## Keep your fork in sync

If you want to contribute again, you need to update your fork to our current state.

1. Fetch all branches from the upstream.<br />
   `git fetch upstream`
2. Rewrite your develop's history with upstream’s develop.<br />
   `git rebase upstream/develop`
3. (Force) push your develop to the remove state.<br />
   `git push origin develop --force`