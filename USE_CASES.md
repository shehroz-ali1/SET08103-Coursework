# Use Cases and Diagram

## 1. Use Case Diagram
The following diagram illustrates the primary actors and their interactions with the World Population Reporting System.

```mermaid
flowchart LR
    Analyst([Organization Analyst])
    User([General User])

    subgraph System [World Population Reporting System]
        UC1(UC1: Generate Country Report)
        UC2(UC2: Generate City Report)
        UC3(UC3: Generate Capital City Report)
        UC4(UC4: Generate Top N Cities Report)
        UC5(UC5: Generate Urban/Rural Report)
        UC6(UC6: Generate Geographical Breakdown)
        UC7(UC7: Generate Language Report)
    end

    Analyst --> UC1
    Analyst --> UC2
    Analyst --> UC3
    User --> UC4
    Analyst --> UC5
    Analyst --> UC6
    Analyst --> UC7
