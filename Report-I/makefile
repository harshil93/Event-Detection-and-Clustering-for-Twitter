PAPER=paper

all:
	pdflatex ${PAPER}
	bibtex   ${PAPER}
	pdflatex ${PAPER}
	pdflatex ${PAPER}
	evince ${PAPER}.pdf

clean:
	rm -f *.aux *.log *.out *.dvi *.ps *.pdf *.toc *.bbl *.blg
