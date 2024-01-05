# MetroPlanner
![MetroPlanner](https://github.com/sanderjurgens/metroplanner/blob/main/img/metroplanner.png)

## What is it?
MetroPlanner is an intuitive application to test (shortest) path planning algorithms.
It consists of a user interface that can load and display user-defined networks, default is the metro network of Paris, and a pair of predefined planning algorithms which can be run on any loaded network.
Both planning algorithms are an implementation of [Breadth-First Search](https://en.wikipedia.org/wiki/Breadth-first_search), working for both directed and undirected (unweighted) graphs.
One planning algorithm minimizes the number stops, whereas the other minimizes the number of transfers.

## Why did I make it?
MetroPlanner is an old (anno 2010) project, from my time studying at [TU/e](https://www.tue.nl/en/), where my passion for complex algorithms and graph theory started.
It was the first project where I combined my learnings about object-oriented programming, writing (Javadoc) documentation, software testing (using JUnit) and algorithms.
All of this eventually culminated in my [Master Thesis](https://research.tue.nl/en/studentTheses/improving-network-robustness) and my first job at [NS](https://www.ns.nl/en), where I continued developing planning optimization algorithms.
Therefore, this project will always hold a special place in my heart.

## How does it work?
If you want to learn more about the codebase, see the [Javadoc](https://sanderjurgens.github.io/metroplanner/).

If you just want to see the application in action, download this [jar](https://github.com/sanderjurgens/metroplanner/blob/main/target/metroplanner-1.1.jar).