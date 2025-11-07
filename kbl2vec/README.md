## KBL to VEC Converter

A detailed description can be found in the Implementation Guidelines of the ECAD-WIKI.

Currently unsupported features (future work):

- Copyright Information for PartVersions
- Changes for Parts & Documents
- Standalone `Module_configuration`s that are not part of a `Module`.
- `PlaceableElementSpecification` and `PlaceableElementRole` are created for all Elements that are potentially placable (from the Model in the KBL). However, this might not be true for everthing. E.g. the KBL says that an `AssemblyPartOccurrence` is a `LocatedComponent` and thus potentially placeable. However, a USB Cable will not be placed at all. May a post processing should be introduced to remove Role & Specs from elements that are not acutally used.

### Architecture

```plantuml
interface Transformer
interface Query
```
