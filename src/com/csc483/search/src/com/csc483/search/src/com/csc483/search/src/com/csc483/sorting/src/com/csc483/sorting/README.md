# CSC483-Algorithms-Assignment-U202255570174

**Course:** CSC 483.1 — Algorithms Analysis and Design
**Student:** Onyekwere Chibueze Favour
**Student ID:** U2022/5570174
**Session:** 2025/2026 | First Semester

---

## Project Structure
src/
├── com/csc483/search/
│   ├── Product.java
│   ├── SearchAlgorithms.java
│   └── TechMartBenchmark.java
└── com/csc483/sorting/
├── SortingAlgorithms.java
└── SortingBenchmark.java
---

## Compilation Instructions

Requires **Java 11 or higher**.

```bash
mkdir -p out
javac -d out src/com/csc483/search/*.java src/com/csc483/sorting/*.java
Run Q1 — TechMart Search Benchmark
java -cp out com.csc483.search.TechMartBenchmark
Run Q2 — Sorting Algorithms Benchmark
java -cp out com.csc483.sorting.SortingBenchmark
Dependencies
Java 11+ (no external libraries required)
JUnit 5 for tests
Known Limitations
addProduct() requires array to have pre-allocated null slots
Benchmark times vary based on JVM warm-up; average of 5 runs is used
Quick Sort uses median-of-three pivot for robustness on sorted data
