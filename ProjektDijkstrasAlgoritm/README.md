# ProjektDijkstrasAlgoritm

The project is a Java-based prototype for handling and analyzing geographical server hall data using graph structures and algorithms. The purpose of the system is to represent server halls as nodes in a weighted graph where connections between server halls can be analyzed and visualized. The application contains functionality for shortest path calculations, nearest neighbor searches, random data generation, file loading and performance testing.

## Project Structure

### app

Contains the entry point of the application and is responsible for starting the system and initializing the different components. This package is used for creating or loading graph data, demonstrating functionality and launching the graphical user interface.

### model

Contains the core data objects used throughout the system. These classes represent only data and properties such as server halls and geographical positions and do not contain advanced application logic.

### graph

Contains the graph structure and graph-related classes. The package is responsible for storing nodes and edges and handling relationships between objects in the network. The graph is implemented using an adjacency list structure to provide efficient memory usage and easy access to neighboring nodes.

### algorithm

Contains implementations of the algorithms used in the system. The package is responsible for calculations such as Dijkstra’s shortest path algorithm and nearest neighbor searches. It also contains helper classes used to store and manage algorithm results.

### data

Responsible for creating and handling input data for the system. The package is primarily used for generating random server halls and connected graphs that are used for testing and benchmark measurements.

### util

Contains helper functionality and reusable utilities used throughout the system. The package includes functionality for file loading and operations related to distance calculations.

### ui

Contains all graphical user interface components. The package is responsible for graph visualization, user interaction and presenting algorithm results without containing the core application logic.

### benchmark

Contains functionality related to performance measurements and execution time testing. The package is used to run benchmark tests on algorithms and data structures using randomly generated datasets and producing result files for later evaluation.

### test

Contains JUnit tests and validation classes used to verify that the functionality of the system behaves correctly. The package is also used to test algorithms, graph structures, file loading and other important parts of the application.

## Running the Project

The application can be started by running the `Main.java` file. The graphical user interface allows the user to visualize graphs, calculate shortest paths, search for nearest server halls, generate random graphs and load graph data from external files.

## Running Benchmarks

Benchmark tests can be executed by running `BenchmarkRunner.java`. The benchmark system automatically generates synthetic data, executes algorithms multiple times and stores performance results in a CSV file located in:

```text
results/benchmark_results.csv
```

## Running Tests

JUnit tests can be executed directly from Eclipse by selecting a test class, right-clicking and choosing:

```text
Run As > JUnit Test
```