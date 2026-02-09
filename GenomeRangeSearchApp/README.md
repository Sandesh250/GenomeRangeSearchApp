# Genome Range Search Engine using Interval Trees

A simple academic web application demonstrating Interval Tree data structure for genomic range queries.

## Project Structure

```
GenomeRangeSearchApp/
├── genome/
│   ├── IntervalNode.java       # BST node for interval tree
│   ├── IntervalTree.java       # Interval tree operations
│   └── GenomeSearchServer.java # Standalone HTTP server
├── index.html                  # Frontend search form
├── styles.css                  # Styling
└── README.md
```

## Tech Stack

- **Frontend**: HTML + CSS (no JavaScript frameworks)
- **Backend**: Java with built-in HttpServer (no external dependencies)
- **Core Logic**: Interval Tree Data Structure

## Quick Start

```bash
# Compile
javac genome\*.java

# Run
java genome.GenomeSearchServer

# Open browser
http://localhost:8080
```

## Sample Dataset

| Gene | Interval | Description |
|------|----------|-------------|
| BRCA1 | [100, 300] | Breast cancer gene 1 |
| TP53 | [250, 400] | Tumor protein p53 |
| MYC | [500, 650] | MYC proto-oncogene |
| EGFR | [700, 900] | Epidermal growth factor receptor |
| KRAS | [850, 1000] | KRAS proto-oncogene |

## Test Queries

| Query | Expected Result |
|-------|-----------------|
| [200, 350] | BRCA1, TP53 |
| [600, 800] | MYC, EGFR |
| [1, 50] | No overlapping genes |
| [850, 900] | EGFR, KRAS |

## Algorithm Details

### Interval Tree

An augmented BST where each node stores:
- `start`, `end`: Interval boundaries
- `maxEnd`: Maximum end value in subtree (optimization key)
- `geneName`: Gene identifier

### Time Complexity

| Operation | Complexity | Notes |
|-----------|------------|-------|
| Insert | O(log n) | Standard BST insertion + maxEnd update |
| Search | O(log n + k) | k = number of overlapping intervals |

### Overlap Condition

Two intervals [s1, e1] and [s2, e2] overlap if:
```
s1 <= e2 AND s2 <= e1
```

