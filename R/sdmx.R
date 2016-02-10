## devtools::install(file.path(dbpath, "GitHub", "SDMX", "RJSDMX"))
library(RJSDMX)
library(ggplot2)
library(dplyr)
library(tidyr)
theme_set(theme_gray(base_size = 18))
## provider <- "ECB"
## query <- "EXR.A.USD+NZD.EUR.SP00.A"
## start <- "2000"
## end <- "2005"

.cache <- new.env()
cache <- function(provider,query,start,end) {
    key <- paste0(provider, "-", query, "-", start, "-", end)
    if(exists(key, envir = .cache)) {
        get(key, envir = .cache)
    } else {
        # d <- na.omit(getWeatherForYear(location, year))

        # maxT <- d[which.max(d$Max_TemperatureC), c("Max_TemperatureC", "Date")]
        # minT <- d[which.min(d$Min_TemperatureC), c("Min_TemperatureC", "Date")]
        
        # p <- ggplot(d, aes(x = Date)) + coord_cartesian(ylim = c(-24, 44)) + # -10, 110
        #     labs(x = "", y = expression(paste("Temperature (", degree, "C)"))) +
        #         geom_line(aes(y = Max_TemperatureC)) +
        #             stat_smooth(se = FALSE, aes(y = Max_TemperatureC), method = "loess", span = 0.3) +
        #                 geom_line(aes(y = Min_TemperatureC)) +
        #                     stat_smooth(se = FALSE, aes(y = Min_TemperatureC), method = "loess", span = 0.3) +
        #                         geom_ribbon(aes(ymin = Min_TemperatureC, ymax = Max_TemperatureC), fill = "tomato", alpha = 0.4)

        # ## fn <- normalizePath(paste0(tempdir(), .Platform$file.sep, key, ".svg"))

        d <- sdmxdf(
            ## getSDMX(provider = "ECB",
            ##         id = "EXR.A.USD+NZD.EUR.SP00.A",
            ##         start = "2000",
            ##         end = "2006")
            getSDMX(provider = provider,
                    id = query,
                    start = start,
                    end = end)
            )

        ## write to file for dygraph
        ## write.csv(d, file = file.path(tempdir(), paste0(key, ".csv")))
        ## write.csv(d, file = paste0(key, ".csv"))
        
        fn <- file.path(tempdir(), paste0(key, ".svg"))

        ## p <- ggplot(d, aes(x = TIME_PERIOD, group = ID)) + geom_line(aes(y = OBS_VALUE, color = ID))
        ## svg(fn, width = 10, height = 4)
        ## print(p)
        ## dev.off()

        d <-
            d %>%
                spread(key = ID, value = OBS_VALUE) %>%
                    mutate(TIME_PERIOD = paste0(TIME_PERIOD, "0101"))

        ## datafile <- file.path("public", "data", "sdmx", paste0(key, ".csv"))
        datafile <- file.path("public", "data", "sdmx", paste0("tsdata", ".csv"))
        unlink(datafile)
        write.csv(d, file = datafile, row.names = FALSE, quote = FALSE)

        fmt <- "%B %e, %Y"
        result <- list(
            filename = fn,
            ## values = d$OBS_VALUES
            # series = toString(unique(d$ID))
            timerange = toString(paste(head(substr(d$TIME_PERIOD, 1, 4), 1), tail(substr(d$TIME_PERIOD, 1, 4), 1), sep = "-")),
            nseries = length(d)-1
            # minValue = minT[1, 1],
            # minDate = format(minT[1, 2], fmt),
            # maxValue = maxT[1, 1],
            # maxDate = format(maxT[1, 2], fmt)
            )
        assign(key, result, envir = .cache)
        ## cat(result$filename)
        result
    }
}
