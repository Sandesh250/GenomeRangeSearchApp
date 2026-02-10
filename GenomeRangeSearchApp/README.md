# 🧬 Genome Range Search Engine

A modern web application demonstrating **Interval Tree** data structure for efficient genomic range queries with a beautiful dashboard UI.

![GitHub](https://img.shields.io/badge/Java-Standalone%20Server-orange)
![Algorithm](https://img.shields.io/badge/Algorithm-Interval%20Tree-blue)
![Complexity](https://img.shields.io/badge/Search-O(log%20n%20%2B%20k)-green)

## ✨ Features

- 🎨 **Modern Dashboard UI** - Card-based layout with grid background
- 🔍 **Efficient Search** - O(log n + k) interval tree algorithm
- 🧬 **37 Diverse Genes** - 6 types: Protein Coding, miRNA, lncRNA, Regulatory, Pseudogene, snoRNA
- 🎯 **Gene Type Filter** - Filter by type with dynamic count updates
- 📊 **Card-Based Results** - Beautiful search results with expandable details
- ➕ **Add New Genomes** - User input form with validation
- 📱 **Responsive Design** - Works on desktop, tablet, and mobile
- 🎭 **Color-Coded Types** - Visual gene type identification

## 🚀 Quick Start

```bash
# Clone the repository
git clone https://github.com/Sandesh250/GenomeRangeSearchApp.git
cd GenomeRangeSearchApp

# Compile
javac genome\*.java

# Run server
java genome.GenomeSearchServer

# Open browser
http://localhost:8080
```

## 📁 Project Structure

```
GenomeRangeSearchApp/
├── genome/
│   ├── IntervalNode.java       # BST node with metadata
│   ├── IntervalTree.java       # Interval tree operations
│   └── GenomeSearchServer.java # HTTP server + API endpoints
├── index.html                  # Dashboard with form
├── styles.css                  # Modern styling
└── README.md
```

## 🧪 Sample Queries

| Query Range | Expected Genes | Count |
|-------------|----------------|-------|
| [200, 350] | BRCA1, TP53 | 2 |
| [600, 800] | MYC, EGFR | 2 |
| [1, 50] | None | 0 |
| [850, 900] | EGFR, KRAS | 2 |

## 🎯 Gene Types

| Type | Count | Badge Color |
|------|-------|-------------|
| Protein Coding | 15 | Blue |
| miRNA | 5 | Purple |
| lncRNA | 5 | Green |
| Regulatory | 5 | Orange |
| Pseudogene | 3 | Pink |
| snoRNA | 4 | Teal |

## 🔧 Tech Stack

- **Frontend**: HTML5, CSS3, Vanilla JavaScript
- **Backend**: Java with built-in `HttpServer` (no external dependencies!)
- **Data Structure**: Augmented Binary Search Tree (Interval Tree)
- **Server**: Standalone HTTP server on port 8080

## 📊 Algorithm Details

### Interval Tree

An augmented BST where each node stores:
- `start`, `end` - Interval boundaries
- `maxEnd` - Maximum end value in subtree (optimization)
- `geneName`, `chromosome`, `geneType`, `function`, `disease`, `applications`

### Time Complexity

| Operation | Complexity | Description |
|-----------|------------|-------------|
| Insert | O(log n) | BST insertion + maxEnd update |
| Search | O(log n + k) | k = overlapping intervals |
| Space | O(n) | n genomic intervals |

### Overlap Condition

Two intervals [s1, e1] and [s2, e2] overlap if:
```
s1 <= e2 AND s2 <= e1
```

## 🌟 Key Features

### 1. Dashboard UI
- Fixed header with gradient background
- Centered search card
- Gene cards in responsive grid
- White background with subtle grid pattern
- Project details section

### 2. Search Functionality
- Input start and end positions
- View results as cards
- Click "More Details" to expand
- Shows overlap length calculation

### 3. Gene Type Filter
- Dropdown with all 6 types
- Instant filtering
- Dynamic gene count updates

### 4. Add New Genome
- Comprehensive form with validation
- AJAX submission to `/addGene` endpoint
- Dynamic card creation
- Success/error feedback

## 🔌 API Endpoints

### `GET /search`
**Parameters:** `queryStart`, `queryEnd`  
**Returns:** HTML page with card-based results

### `POST /addGene`
**Parameters:** `geneName`, `startPos`, `endPos`, `chromosome`, `geneType`, `function`, `disease`, `applications`  
**Returns:** JSON response with success status

## 📸 Screenshots

### Main Dashboard
- 37 gene cards in grid layout
- Color-coded type badges
- Search and filter sections

### Search Results
- Card-based results
- Expandable details
- Overlap calculation

### Add Genome Form
- All required fields
- Validation feedback
- Success animation

## 🎓 Educational Value

This project demonstrates:
- **Data Structures**: Interval Tree implementation
- **Algorithm Design**: Efficient range query optimization
- **Web Development**: Full-stack application without frameworks
- **UI/UX**: Modern dashboard design principles
- **API Design**: RESTful endpoints with JSON responses

## 📝 License

This is an academic project for educational purposes.

## 👨‍💻 Author

**Sandesh**  
GitHub: [@Sandesh250](https://github.com/Sandesh250)

---

⭐ Star this repo if you found it helpful!
