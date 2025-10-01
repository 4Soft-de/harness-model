## KBL to VEC Converter

A detailed description can be found in the Implementation Guidelines of the ECAD-WIKI.

Currently unsupported features (future work):

- Translation of Processing Instructions and Installation Instructions --> CustomerProperties.
- Copyright Information for PartVersions
- Changes for Parts & Documents
- Standalone `Module_configuration`s that are not part of a `Module`.
- Multi core wires

### Architecture

```plantuml
interface Transformer
interface Query
```
