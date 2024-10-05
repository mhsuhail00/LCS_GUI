# Longest Common Subsequence (LCS) Using Dynamic Programming (DP) with Memoization
Longest Common Subsequence Graphical User Interface. The Backend Algorithm is based on Dynamic-Programming Approach with Memoization. It takes Sequence in which the terms are String of any length, Need Not Single Character.

## Table of Contents
- [Introduction](#introduction)
- [Problem Definition](#problem-definition)
- [Approach](#approach)
  - [Recursive Solution](#recursive-solution)
  - [Dynamic Programming with Memoization](#dynamic-programming-with-memoization)
- [Algorithm Explanation](#algorithm-explanation)
  - [Key Rules](#key-rules)
  - [Steps to Compute LCS](#steps-to-compute-lcs)
- [Code Example](#code-example)
- [How to Run](#how-to-run)
- [Contributing](#contributing)
- [License](#license)

## Introduction

This repository demonstrates an optimized solution to find the **Longest Common Subsequence (LCS)** between two sequences using **Dynamic Programming (DP)** with **Memoization**. The LCS problem is a classic algorithmic challenge that finds the longest subsequence common to two sequences, which appears in both in the same order, but not necessarily consecutively.

## Problem Definition

Given two sequences:

- **Xm = <x₁, x₂, x₃, ..., xm>**
- **Yn = <y₁, y₂, y₃, ..., yn>**

The goal is to find the **Longest Common Subsequence (LCS)**:

- **Zk = <z₁, z₂, z₃, ..., zk>**

The problem can be solved efficiently using **Dynamic Programming with Memoization**, where overlapping subproblems are stored in a 2D array for reuse.

## Approach

### Recursive Solution

The LCS problem can be solved using a naive recursive approach, but it leads to excessive recomputation of subproblems, making it inefficient for large sequences.

### Dynamic Programming with Memoization

By storing the results of overlapping subproblems, **Memoization** significantly reduces the time complexity. We define a 2D array `C[i, j]` where each entry stores the length of the LCS for the subsequences `X[0...i]` and `Y[0...j]`.

## Algorithm Explanation

### Key Rules

1. If `xm == yn`, then:
   - Append `xm` to the LCS of `Xm-1` and `Yn-1`.
2. If `xm != yn`, solve the following subproblems:
   - LCS of `Xm-1` and `Yn`
   - LCS of `Xm` and `Yn-1`
   
This approach ensures that overlapping subproblems are efficiently computed and stored.

### Steps to Compute LCS:

1. Define a 2D array `C` of size `(m+1, n+1)` to store memoized values, where `m` and `n` are the lengths of sequences `X` and `Y`, respectively.
2. The value of `C[m, n]` will hold the length of the LCS.
3. The recursive formula to fill the array `C` is:
   - `C[i, j] = 0`, if `i == 0` or `j == 0`
   - `C[i, j] = C[i-1, j-1] + 1`, if `X[i-1] == Y[j-1]`
   - `C[i, j] = max(C[i-1, j], C[i, j-1])`, if `X[i-1] != Y[j-1]`
