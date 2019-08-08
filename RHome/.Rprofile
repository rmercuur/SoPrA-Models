# This is the first thing R will run when it starts.

.First <- function() {

  # make sure to load libraries before doing evaluations below.
  library(utils)

  # Load in the Repast outputer files that are specified in the runtime wizard
  fileNumber <- 0;

  while ("" != eval.parent(parse(text=sprintf("Sys.getenv('LOG_FILE%s')" , fileNumber)))) {
	fileName <- eval.parent(parse(text=sprintf("Sys.getenv('LOG_FILE%s')" , fileNumber)));
				
	varName <- eval.parent(parse(text=sprintf("gsub('[^[:alnum:]]', '', Sys.getenv('LOG_FILE%s'))" , fileNumber)));
				
	delimiter <- eval.parent(parse(text=sprintf("Sys.getenv('DELIMITER%s')" , fileNumber)));

	try(
        eval(parse(text=sprintf("%s=read.table('%s', header=TRUE, sep='%s')" , varName, fileName, delimiter)), .GlobalEnv)
      )

	fileNumber <- fileNumber + 1;
  }
}
