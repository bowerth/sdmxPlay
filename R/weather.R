library(weatherData)
library(ggplot2)
theme_set(theme_gray(base_size = 18))

.cache <- new.env()
cache <- function(location,year) {
    key <- paste0(location, "-", year)
    if(exists(key, envir = .cache)) {
        get(key, envir = .cache)
    } else {
        d <- na.omit(getWeatherForYear(location, year))

        maxT <- d[which.max(d$Max_TemperatureC), c("Max_TemperatureC", "Date")]
        minT <- d[which.min(d$Min_TemperatureC), c("Min_TemperatureC", "Date")]
        
        p <- ggplot(d, aes(x = Date)) + coord_cartesian(ylim = c(-24, 44)) + # -10, 110
            labs(x = "", y = expression(paste("Temperature (", degree, "C)"))) +
                geom_line(aes(y = Max_TemperatureC)) +
                    stat_smooth(se = FALSE, aes(y = Max_TemperatureC), method = "loess", span = 0.3) +
                        geom_line(aes(y = Min_TemperatureC)) +
                            stat_smooth(se = FALSE, aes(y = Min_TemperatureC), method = "loess", span = 0.3) +
                                geom_ribbon(aes(ymin = Min_TemperatureC, ymax = Max_TemperatureC), fill = "tomato", alpha = 0.4)

        ## fn <- normalizePath(paste0(tempdir(), .Platform$file.sep, key, ".svg"))
        fn <- file.path(tempdir(), paste0(key, ".svg"))
        svg(fn, width = 6, height = 4)
        print(p)
        dev.off()

        fmt <- "%B %e, %Y"
        result <- list(filename = fn,
                       minValue = minT[1, 1],
                       minDate = format(minT[1, 2], fmt),
                       maxValue = maxT[1, 1],
                       maxDate = format(maxT[1, 2], fmt))
        assign(key, result, envir = .cache)
        result
    }
}
