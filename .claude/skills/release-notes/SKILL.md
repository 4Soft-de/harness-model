---
name: release-notes
description: Draft the GitHub release notes for a harness-model release. Use when asked to write, prepare, update or review the notes for a new version, or when a release is being cut.
---

# Release notes for harness-model

The notes for a release are not only read on the release page. **Renovate and Dependabot embed them
into the update pull requests of every consumer**, which is where most people decide whether to take
the upgrade. Anything that does not render there, or that does not carry enough information to
decide, costs the consumer exactly the thing the notes exist for.

That is why the format is fixed and why the wording rules below are stricter than they would need to
be for a page somebody visits deliberately.

Use the body of the previous release as the template:

```bash
curl -s https://api.github.com/repos/4Soft-de/harness-model/releases/tags/<previous-version>
```

## Gather the material first

1. **The previous release** — its `published_at` bounds everything else, and its `body` is the
   template.
2. **The pull requests merged since then**, for the module, the description, the author and the
   link:
   `https://api.github.com/repos/4Soft-de/harness-model/pulls?state=closed&base=develop&per_page=100`
   filtered by `merged_at` later than the previous release. `gh pr list --state merged` does the
   same where `gh` is installed.
3. **The commit bodies**: `git log <previous-tag>..develop`. This is where the concrete figures
   live — allocation, heap, runtime, counts. A bullet that carries a measured number is worth far
   more than one that says "improved performance", and the number is almost always already written
   down in the commit that made the change.

Do not reconstruct the release from the commit subjects alone. Merges are not always squashed, so a
single pull request can appear as several commits, and the commit subject does not carry the pull
request number in that case.

## Structure

In this order, omitting every section that would be empty:

| Section | Contents |
|---|---|
| `> [!IMPORTANT]` callout | What hurts when upgrading. No callout when nothing does. |
| Intro paragraph | Optional. One or two sentences on what the release is about. |
| `## 💥 Breaking Changes` | |
| `## 🚀 New Features` | Swap the heading for what dominates the release where that fits better — 6.2.0 used `## 🚀 Performance`. |
| `## ⚠️ Deprecations` | Each entry names its replacement. |
| `## 🔧 Migration` | Present whenever a consumer has to change their own code. See below. |
| `## 🔎 Compatibility Notes` | Everything a consumer can observe that is not a new feature or a fix. See below. |
| `## 🐛 Bug Fixes` | |
| `## 📖 Documentation` | |
| `## 📦 Dependency Updates` | `* Name old → new in <link>`, no author. Mark major bumps and name the module they affect. Write `* None — this release contains no dependency changes.` rather than dropping the section. |
| `<details><summary>Build, test & CI updates</summary>` | Everything that does not reach a consumer: build plugins, test dependencies, workflows, benchmarks. Collapsed, so the embedded body stays short in the consumer's pull request. |
| `**Full Changelog**` | `https://github.com/4Soft-de/harness-model/compare/<previous>...<new>` |

## Mechanical rules

These exist because of how the notes are rendered elsewhere:

- **Tag name, release title and Maven version are identical and carry no `v` prefix.** Renovate
  matches on that; with a `v` it does not find the notes at all.
- **Only `##` headings, never `#`.** The embedding tools put the body underneath their own heading.
- **Every link is absolute** (`https://github.com/4Soft-de/harness-model/...`), including links to
  files in the repository (`blob/develop/docs/...`). A repo-relative link is dead once the notes are
  embedded somewhere else.
- **Bullet form:** `* **module**: Description by @author in <full pull request link>`. Several pull
  requests for one bullet are appended comma-separated.
- The module prefix is the part of the repository a consumer recognises: `navext`, `vec`, `kbl`,
  `compatibility`, `kbl2vec`, `vec-rdf`, or `All model modules` where it applies everywhere.

## Migration instructions

Whenever this release forces a consumer to touch their own code — a deprecation, a removal, a changed
default, a signature that moved — the notes carry a `## 🔧 Migration` section, and that section has
to be usable **without reading the source of this repository**.

The audience is not only a human maintainer. Renovate and Dependabot open the upgrade pull request in
the consumer's repository, and that pull request is increasingly reviewed by an agent — usually after
the build has already failed. Whatever it needs in order to fix that failure has to be in the notes,
because the embedded notes are the only thing about this release it gets to see.

For every change that requires action, give:

- **The symptom.** The compiler message, the linter rule id, the exception — the text somebody will
  actually be looking at. `warning: [deprecation] VecNavs in com.foursoft.harness.vec.v12x.navigations
  has been deprecated` is worth more than "the navigation API changed".
- **Old to new, concretely.** Name the type and the method, not just the package. A replacement that
  is mechanical belongs in the notes *as* a mechanical replacement, so that it can be applied without
  understanding the design behind it.
- **Whether it can wait.** A deprecation whose body still delegates can be migrated later; a removal
  cannot. Say which one it is, and name the release in which the other shoe drops.
- **The escape hatch, if there is one, and what it costs** — a suppression, a compiler flag, staying
  on the old call for one more release.

**A deprecation is a build break for some consumers.** A project that compiles with `-Xlint:all` and
`failOnWarning` fails on a newly introduced deprecation warning even though nothing about the
behaviour changed, and several consumers of these libraries are built that way. Do not call such a
release a drop-in upgrade without qualifying it: it is drop-in *at runtime*, and it still needs
either a migration or a suppression before it compiles. 6.1.0 deprecated the `*Navs` catalogs and
described itself as a drop-in upgrade; for a consumer with that build setup it was not one.

## Rules for numbers and claims

This is where release notes actually go wrong. Every one of these comes from a mistake that was
shipped.

**Every number says what it counts and over what.** "183 back-reference sets" is meaningless:
183 *fields across the classes of a model* and 183 *sets per object* differ by more than an order of
magnitude. Write the scope into the sentence — per conversion, per instance, across the model, over
the whole heap.

**Allocation and retained heap are different things and are never both called "memory".** Allocation
is throughput: bytes that passed through the JVM over the run, most of them collected immediately.
Retained heap is what is still live. A reader who takes an allocation figure for a footprint will
conclude the library needs 100 MB to hold a 6.7 MB document.

**A figure in the summary covers the whole release, or it names the change it belongs to.** Numbers
measured for one pull request, presented as the headline of a release that merged several, understate
the release and contradict the bullets further down.

**Say where a number was measured.** A sample document in the test resources and a production export
are not the same evidence. Name which one it was.

**The summary must not claim behavioural stability while the notes describe a behavioural change.**
Before handing the notes over, read the `[!IMPORTANT]` callout and the Compatibility Notes against
the intro paragraph. If they disagree, the intro is wrong.

**Semantic changes go into Compatibility Notes even when no signature changed.** A cache that starts
comparing by identity instead of by `equals`, a field that is `null` until first access, a method
that is no longer `synchronized` — none of those change an API, and all of them can change what a
consumer observes. If a reasonable consumer could notice it, it belongs in that section, with the
condition under which it is harmless.

## Before handing it over

- Every bullet has a module prefix, an author and a pull request link.
- Every link is absolute.
- Every number names its scope and its source.
- Every change that forces a consumer to touch their code has a migration entry, with the
  message they will see and the replacement to apply.
- The intro, the callout and the Compatibility Notes tell the same story.
- Sections that would be empty are gone; `📦 Dependency Updates` says "None" instead.
- The `compare/` link uses the two right tags.

Write the draft to a file and hand it to the maintainer. **Do not create or edit the GitHub release
yourself** — publishing is the maintainer's step.
