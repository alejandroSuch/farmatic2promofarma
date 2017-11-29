# farmatic 2 promofarma

## Variables

`farmatic.stock.factor`: factor to apply to stoc. Values should be between 0 and 1 (i.e.: 0.7). Default value: 0. Decimal values will apply a `ceil`.

`farmatic2csv.use.price`: add price column to CSV. Default value: false

`farmatic2csv.job.delay`: number of milliseconds to wait between csv generations. Default value: `300000` (five minutes).

`farmatic2csv.out.file`: output file name. Default: `/path/to/temp/dir/farmatic2promofarma.csv`

`spring.datasource.url`: URL to the datasource. No default value. Example: 

`jdbc:sqlserver://localhost:1433;databaseName=FARMACIA`

`spring.datasource.username`: Database username. No default value.

`spring.datasource.password`: Database password. No default value.
