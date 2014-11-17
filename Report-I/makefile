all: cover.pdf paper.pdf certi.pdf

cover.pdf: cover.tex
	pdflatex cover

certi.pdf: certi.tex
	pdflatex certi

paper.pdf: paper.tex cover.pdf certi.pdf review.tex abstract.tex problem.tex introduction.tex ecosystem.tex twitterlda.tex paper.bib phase2.tex experiments.tex evolution.tex table.tex
	pdflatex paper
	bibtex   paper
	pdflatex paper
	pdflatex paper
	evince paper.pdf
	# make clean


clean:
	rm -f *.aux *.log *.out *.dvi *.ps *.pdf *.toc *.bbl *.blg
