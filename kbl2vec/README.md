## KBL to VEC Converter

A detailed description can be found in the Implementation Guidelines of the ECAD-WIKI.

Currently unsupported features (future work):

- Copyright Information for PartVersions
- Changes for Parts & Documents
- Standalone `Module_configuration`s that are not part of a `Module`.
- `PlaceableElementSpecification` and `PlaceableElementRole` are created for all Elements that are potentially
  placable (from the Model in the KBL). However, this might not be true for everthing. E.g. the KBL says that an
  `AssemblyPartOccurrence` is a `LocatedComponent` and thus potentially placeable. However, a USB Cable will not be
  placed at all. May a post processing should be introduced to remove Role & Specs from elements that are not acutally
  used.
- `TerminalRole` without Specification; can't be derived from KBL data
- Placement of `ComponentBoxConnector`
- Harness Configuration
- Module Family
- Fuse
- Component
- Coupling

### Architecture

```mermaid
classDiagram
    class ConversionOrchestrator {
        -Class sourceClass
        -Class destinationClass
        -TransformerRegistry transformerRegistry
        -TransformationContext transformationContext
        -Queue~TransformationHolder~ transformations
        -Queue~FinisherHolder~ finisher
        -List~Processor~ postProcessors
        -List~Processor~ preProcessors
        -Map~Object, String~ comments
        +orchestrateTransformation(source)
        +addPostProcessor(postProcessor)
        +addPreProcessor(preProcessor)
    }
    class ConversionProperties {
        -VecLanguageCode defaultLanguageCode
        -String defaultMassInformationSource
        -VecValueDetermination defaultValueDetermination
        -String defaultColorReferenceSystem
        -String defaultWireTypeReferenceSystem
        -String defaultMaterialReferenceSystem
        -String defaultExternalReferenceCompanyName
        -Clamping default3DCurveClamping
        +get/set methods
    }
    class EntityMapping {
        -Multimap~Object, Object~ mappedElements
        +put(sourceEntity, destinationEntity)
        +getContent()
        +getIfUniqueOrElseThrow(sourceEntity, destinationClass)
    }
    class Processor~D~ {
        +apply(source, context)
    }
    class Transformation~S, D~ {
        +sourceClass
        +destinationClass
        +sourceQuery
        +accumulator
    }
    class TransformationContext {
        +getEntityMapping()
        +getConversionProperties()
        +getConverterRegistry()
        +getLogger()
        +getNewId()
    }
    class TransformationContextImpl {
        -ConversionProperties conversionProperties
        -ConverterRegistry converterRegistry
        -EntityMapping entityMapping
        -Logger logger
        -AtomicInteger idCounter
        +implements TransformationContext
    }
    class TransformationFragment~D, B~ {
        +performFragment(resultValue, builder)
    }
    class TransformationResult~D~ {
        +element
        +downstreamTransformations
        +finisher
        +comments
        +isEmpty()
        +static Builder
    }
    class TransformationResult.Builder~D~ {
        +withDownstream(...)
        +withComment(...)
        +withLinker(...)
        +withFragment(...)
        +withFinisher(...)
        +build()
    }
    class Finisher {
        +finishTransformation(context)
    }
    class LinkingFinisher~S, D~ {
        -Query~S~ sourceObjects
        -Class~D~ targetClass
        -Consumer~D~ linker
        +finishTransformation(context)
        +implements Finisher
    }
    class Query~T~ {
        +execute()
        +stream()
        +static of(...)
        +static fromLists(...)
    }
    class Transformer~S, D~ {
        +transform(context, source)
    }
    class TransformerRegistry {
        +getTransformer(source, destination)
    }

    ConversionOrchestrator --> TransformerRegistry
    ConversionOrchestrator --> TransformationContext
    ConversionOrchestrator --> Processor
    ConversionOrchestrator --> EntityMapping
    ConversionOrchestrator --> ConversionProperties
    ConversionOrchestrator --> Transformation
    ConversionOrchestrator --> Finisher
    ConversionOrchestrator --> TransformationResult
    ConversionOrchestrator --> TransformationStackTrace
    ConversionOrchestrator --> TransformationStackTraceElement

    TransformationContext <|.. TransformationContextImpl
    TransformationContextImpl --> EntityMapping
    TransformationContextImpl --> ConversionProperties
    TransformationContextImpl --> ConverterRegistry

    EntityMapping --> ConversionException

    TransformationResult --> Transformation
    TransformationResult --> Finisher
    TransformationResult --> TransformationResult.Builder
    TransformationResult.Builder --> LinkingFinisher
    TransformationResult.Builder --> TransformationFragment

    LinkingFinisher ..|> Finisher

    Processor <|.. ConversionOrchestrator
    Finisher <|.. ConversionOrchestrator

    TransformerRegistry --> Transformer

    Query --> Transformation
    Query --> LinkingFinisher

```
