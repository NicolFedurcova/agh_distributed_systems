# Distributed Systems – Coursework

This repository contains a collection of six assignments completed as part of the **Distributed Systems** course under the supervision of **Prof. Nabendu Chaki**, University of Calcutta.  
The projects explore fundamental algorithms and coordination mechanisms used in distributed systems — covering mutual exclusion, deadlock detection, token passing, and graph-based communication.

Each assignment folder includes:
- Source code implementation
- Problem statement PDF 
- Example input files, outputs and intermediate states

---

## Assignment Overview

| # | Topic | Description |
|---|--------|-------------|
| **1** | **Reaching All Nodes in a Directed Graph** | Implemented a generic algorithm to determine whether a given node can reach all other nodes in a connected directed graph. The program also identifies all nodes that can act as initiators in distributed algorithms such as Chandy–Lamport snapshots or leader election protocols. |
| **2** | **Ricart–Agrawala’s Symmetric Algorithm** | Simulated the Ricart–Agrawala mutual exclusion algorithm for a distributed system with 4–6 nodes. Demonstrates message-based coordination where each process requests permission before entering the critical section without using a central coordinator. |
| **3** | **Raymond’s Algorithm** | Implemented Raymond’s tree-based mutual exclusion algorithm on an inverted tree structure. Each node tracks its parent and request queues are maintained both locally and in predecessor nodes. The system simulates multiple concurrent requests and shows how the token is passed until all requests are satisfied. |
| **4** | **Token-Based Algorithm on Ring Topology** | Developed a token-passing coordination mechanism across a ring network with *N* nodes and *M* requests. Each node communicates only with its one-hop neighbor. The algorithm maintains local queues for token requests and demonstrates token circulation and queue updates after each critical section execution. |
| **5** | **Mitchell–Merritt Deadlock Detection Algorithm** | Simulated distributed deadlock detection using the Mitchell–Merritt algorithm. Nodes maintain unique cryptographic keys, establish block rules between pairs, and apply the **Transmit** rule dynamically to identify and resolve potential deadlocks in a distributed environment. |
| **6** | **Centralized Distributed Deadlock Detection (DDD) Algorithm** | Implemented a centralized deadlock detection mechanism. Each site maintains resource and process status tables, which are periodically collected by a central controller. The controller builds a Wait-For Graph (WFG) to detect cycles indicating deadlock conditions among distributed processes. |

---

## Technologies & Concepts

- **Languages:** Java
- **Key Concepts:** Distributed mutual exclusion, token-based synchronization, graph traversal, message passing, deadlock detection, wait-for graphs, concurrency control.
- **System Models:** Tree topology, ring topology, centralized controller, distributed sites.

---

## Learning Outcomes

- Gained hands-on experience with core distributed algorithms and synchronization techniques.  
- Developed understanding of process coordination and communication in asynchronous environments.  
- Applied theoretical concepts to practical simulations demonstrating system state evolution and fault-tolerant behavior.  

---

## Instructor

**Prof. Nabendu Chaki**  
Department of Computer Science, University of Calcutta  
[![Google Scholar](https://img.shields.io/badge/Google%20Scholar-Profile-blue?logo=googlescholar&logoColor=white)](https://scholar.google.com/citations?user=H4_XbtsAAAAJ&hl=en)
---
## Course
***Distributed Systems***, Akademia Górniczo-Hutnicza im. Stanisława Staszica w Krakowie

[Distributed Systems – Official Syllabus →](https://sylabusy.agh.edu.pl/en/2/2/20/1/9/55/137#nav-tab-22)


---

### Keywords

`Distributed Systems` • `Mutual Exclusion` • `Deadlock Detection` • `Token Passing` • `Graph Algorithms` • `Concurrency Control` • `Synchronization`

