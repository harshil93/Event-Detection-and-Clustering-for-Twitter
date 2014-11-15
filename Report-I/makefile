all: cover.pdf paper.pdf certi.pdf

cover.pdf: cover.tex
	pdflatex cover

certi.pdf: certi.tex
	pdflatex certi

paper.pdf: paper.tex cover.pdf certi.pdf review.tex abstract.tex problem.tex introduction.tex ecosystem.tex twitterlda.tex
	pdflatex paper
	bibtex   paper
	pdflatex paper
	pdflatex paper
	evince paper.pdf

clean:
	rm -f *.aux *.log *.out *.dvi *.ps *.pdf *.toc *.bbl *.blg
