#!/bin/bash

latex paper.tex
bibtex paper
latex paper.tex
pdflatex paper.tex
